package br.ufpb.dcx.iago.lojadejogos.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Serviço responsável por gerar e validar tokens JWT.
 * O token carrega o email do usuário (subject) e o role (claim "role").
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    /**
     * Gera a chave secreta HMAC a partir da string configurada no application.properties.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gera um token JWT contendo o email como subject e a role como claim customizada.
     */
    public String gerarToken(String email, String role) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrai o email (subject) do token JWT.
     */
    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    /**
     * Extrai a role do token JWT.
     */
    public String extrairRole(String token) {
        return extrairClaims(token).get("role", String.class);
    }

    /**
     * Verifica se o token está válido (não expirado e assinatura correta).
     */
    public boolean isTokenValido(String token) {
        try {
            extrairClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Método interno que faz o parse do token e retorna as claims.
     * Se o token for inválido ou expirado, lança exceção automaticamente.
     */
    private Claims extrairClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
