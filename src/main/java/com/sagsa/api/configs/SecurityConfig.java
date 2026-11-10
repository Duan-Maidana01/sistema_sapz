package com.sagsa.api.configs; // Pacote de configurações da aplicação

import java.util.List; // Lista de origens permitidas

import org.springframework.beans.factory.annotation.Autowired; // Injeção automática de dependências
import org.springframework.beans.factory.annotation.Value; // Leitura de propriedades do application.properties
import org.springframework.context.annotation.Bean; // Indica que o método retorna um Bean gerenciado pelo Spring
import org.springframework.context.annotation.Configuration; // Marca a classe como configuração Spring
import org.springframework.http.HttpMethod; // Enumeração com os métodos HTTP
import org.springframework.http.HttpStatus; // Códigos de status HTTP
import org.springframework.http.MediaType; // Tipos de conteúdo HTTP
import org.springframework.security.authentication.AuthenticationManager; // Gerenciador de autenticação
import org.springframework.security.config.Customizer; // Utilitário de configuração do Spring Security
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // Construtor de autenticação
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Habilita @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Regras de segurança HTTP
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Suporte de segurança Web
import org.springframework.security.config.http.SessionCreationPolicy; // Política de sessão
import org.springframework.security.core.userdetails.UserDetailsService; // Serviço de dados do usuário
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Codificador BCrypt
import org.springframework.security.web.SecurityFilterChain; // Cadeia de filtros
import org.springframework.web.cors.CorsConfiguration; // Política de CORS
import org.springframework.web.cors.CorsConfigurationSource; // Provedor da configuração de CORS
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Mapeador de CORS por URL

import com.sagsa.api.security.JWTAuthenticationFilter; // Filtro de login (/login)
import com.sagsa.api.security.JWTAuthorizationFilter; // Filtro de autorização por token
import com.sagsa.api.security.JWTUtil; // Utilitário JWT

// Classe central de configuração da segurança do Spring Security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Guarda a referência do AuthenticationManager configurado
    private AuthenticationManager authenticationManager;

    // Serviço customizado que busca o usuário na tabela "usuarios" pelo e-mail
    @Autowired
    private UserDetailsService userDetailsService;

    // Utilitário JWT
    @Autowired
    private JWTUtil jwtUtil;

    // Origens do front-end liberadas no CORS
    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    // URLs públicas para qualquer método HTTP
    private static final String[] PUBLIC_MATCHERS = {
        "/",
        "/error"
    };

    // URLs públicas exclusivas para POST (login). O cadastro de usuários é restrito ao ADMIN.
    private static final String[] PUBLIC_MATCHERS_POST = {
        "/login"
    };

    // Configura a cadeia de filtros de segurança HTTP da aplicação
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Obtém o construtor do gerenciador de autenticação compartilhado
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // Define o UserDetailsService e o BCrypt para validar o login
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());

        // Constrói o AuthenticationManager
        this.authenticationManager = authenticationManagerBuilder.build();

        // Ativa o CORS utilizando o Bean corsConfigurationSource()
        http.cors(Customizer.withDefaults());

        // CSRF desabilitado: API REST stateless com JWT
        http.csrf(csrf -> csrf.disable());

        // Nenhuma sessão HTTP é criada no servidor
        http.sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Regras de autorização das requisições
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Pré-voo do CORS
            .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() // Login
            .requestMatchers(PUBLIC_MATCHERS).permitAll() // Rota raiz e página de erro
            .anyRequest().authenticated() // Demais rotas exigem token JWT válido
        );

        // Retorna 401 em JSON quando a requisição não possui token válido
        http.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"status\":401,\"message\":\"Não autenticado. Informe um token JWT válido.\"}");
        }));

        // Seta o gerenciador de autenticação construído
        http.authenticationManager(this.authenticationManager);

        // Filtro de login (JWTAuthenticationFilter) que escuta POST /login
        http.addFilter(new JWTAuthenticationFilter(this.authenticationManager, this.jwtUtil));

        // Filtro de autorização (JWTAuthorizationFilter) que valida o token em cada requisição
        http.addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.jwtUtil, this.userDetailsService));

        return http.build();
    }

    // Configuração explícita de CORS para o front-end
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(this.allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        // Expõe o cabeçalho Authorization para o JavaScript ler o token após o login
        configuration.setExposedHeaders(List.of("Authorization", "Location"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Bean do codificador BCrypt usado no login e no cadastro de usuários
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
