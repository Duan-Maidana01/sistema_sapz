package com.sagsa.api.security; // Pacote de segurança

import java.io.IOException; // Erros de leitura do corpo
import java.util.ArrayList; // Lista vazia de authorities
import java.util.LinkedHashMap; // Corpo JSON ordenado
import java.util.Map; // Estrutura do corpo de resposta

import org.springframework.http.HttpStatus; // Status HTTP
import org.springframework.http.MediaType; // Tipo de conteúdo
import org.springframework.security.authentication.AuthenticationManager; // Gerenciador de autenticação
import org.springframework.security.authentication.AuthenticationServiceException; // Erro ao ler credenciais
import org.springframework.security.authentication.DisabledException; // Usuário inativo
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Token de credenciais
import org.springframework.security.core.Authentication; // Resultado da autenticação
import org.springframework.security.core.AuthenticationException; // Falha de autenticação
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filtro base do login

import com.fasterxml.jackson.databind.ObjectMapper; // Conversão JSON

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.sagsa.api.models.dtos.LoginDTO; // Corpo do login { "email", "senha" }

// Filtro que intercepta POST /login, valida e-mail/senha e devolve o token JWT
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/login");
    }

    // Lê o JSON { "email": "...", "senha": "..." } e tenta autenticar
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginDTO login = this.objectMapper.readValue(request.getInputStream(), LoginDTO.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    login.getEmail(),
                    login.getSenha(),
                    new ArrayList<>()
            );
            return this.authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Corpo do login inválido. Envie { \"email\": \"...\", \"senha\": \"...\" }", e);
        }
    }

    // Login com sucesso: token no cabeçalho Authorization e dados básicos no corpo
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authResult) throws IOException, ServletException {
        UserSpringSecurity userSS = (UserSpringSecurity) authResult.getPrincipal();
        String token = this.jwtUtil.generateToken(userSS.getUsername(), userSS.getPerfil().name());

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("token", token);
        body.put("tipo", "Bearer");
        body.put("expiraEmMs", this.jwtUtil.getExpiration());
        body.put("id", userSS.getId());
        body.put("nome", userSS.getNome());
        body.put("email", userSS.getUsername());
        body.put("perfil", userSS.getPerfil().name());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(this.objectMapper.writeValueAsString(body));
    }

    // Login com falha: 401 em JSON no mesmo padrão do ErrorResponse
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        String message = (failed instanceof DisabledException)
                ? "Usuário inativo. Procure o administrador do sistema."
                : "E-mail ou senha inválidos.";
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("message", message);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(this.objectMapper.writeValueAsString(body));
    }
}
