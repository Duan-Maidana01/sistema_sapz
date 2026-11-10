package com.sagsa.api.configs; // Pacote de configurações da aplicação

import org.springframework.beans.factory.annotation.Value; // Leitura de propriedades
import org.springframework.context.annotation.Configuration; // Classe de configuração do Spring
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Registro de políticas CORS
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Customização do Spring MVC

// Regras de CORS na camada MVC (complementa a configuração do SecurityConfig).
// Observação: @EnableWebMvc NÃO é usado para manter a auto-configuração do Spring Boot
// (ex.: datas ISO no JSON definidas em spring.jackson.*).
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Todas as rotas da API
                .allowedOrigins(this.allowedOrigins) // Origens do front-end
                .allowedHeaders("*") // Qualquer cabeçalho (incluindo Authorization)
                .exposedHeaders("Authorization", "Location") // Cabeçalhos legíveis pelo JavaScript
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowCredentials(true);
    }
}
