package com.sagsa.api.security; // Pacote de segurança

import java.io.IOException;
import java.util.Objects;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro que lê o cabeçalho "Authorization: Bearer <token>" e autentica a requisição
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
            UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (Objects.nonNull(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            UsernamePasswordAuthenticationToken auth = getAuthentication(token);
            if (Objects.nonNull(auth)) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    // Valida o token e carrega o usuário do banco (usuários desativados perdem o acesso imediatamente)
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (this.jwtUtil.isValidToken(token)) {
            String username = this.jwtUtil.getUsername(token);
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (userDetails.isEnabled()) {
                    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                }
            } catch (UsernameNotFoundException e) {
                return null;
            }
        }
        return null;
    }
}
