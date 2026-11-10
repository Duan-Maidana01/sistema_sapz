package com.sagsa.api.security; // Pacote de segurança

import java.util.Date; // Datas de emissão/expiração
import java.util.Objects; // Checagem de nulos

import javax.crypto.SecretKey; // Chave HMAC

import org.springframework.beans.factory.annotation.Value; // Leitura de propriedades
import org.springframework.stereotype.Component; // Componente gerenciado pelo Spring

import io.jsonwebtoken.Claims; // Conteúdo (payload) do token
import io.jsonwebtoken.Jwts; // Construtor/parser JWT
import io.jsonwebtoken.security.Keys; // Geração da chave

// Utilitário para gerar e validar tokens JWT
@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Gera a chave HMAC a partir do segredo
    private SecretKey getKeyToSign() {
        return Keys.hmacShaKeyFor(this.secret.getBytes());
    }

    // Gera o token com o e-mail do usuário como "subject" e o perfil como claim
    public String generateToken(String username, String perfil) {
        SecretKey key = getKeyToSign();
        return Jwts.builder()
                .subject(username)
                .claim("perfil", perfil)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.expiration))
                .signWith(key)
                .compact();
    }

    // Verifica assinatura, subject e expiração do token
    public boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            return Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate);
        }
        return false;
    }

    // Extrai o e-mail (subject) do token
    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            return claims.getSubject();
        }
        return null;
    }

    public Long getExpiration() {
        return this.expiration;
    }

    // Decodifica o token; retorna null se for inválido ou adulterado
    private Claims getClaims(String token) {
        try {
            SecretKey key = getKeyToSign();
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
