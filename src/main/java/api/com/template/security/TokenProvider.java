package api.com.template.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Cuida de toda a parte de JWT: gera o access token e o refresh token no login
 * e le/valida os tokens nas requisicoes. Tambem extrai o email e o id a partir
 * do token. Os parametros vem das propriedades app.jwt.* via @Value.
 */
@Slf4j
@Component
public class TokenProvider {

    private static final String CLAIM_ID = "id";
    private static final String CLAIM_PERFIL = "perfil";
    private static final String CLAIM_TIPO = "tipo";
    private static final String TIPO_REFRESH = "refresh";

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expirationsMs;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiracaoMs;

    @Value("${app.jwt.issuer}")
    private String issuer;

    public String generateAccessToken(UsersDetails usersDetails) {
        return build(usersDetails, expirationsMs, false);
    }

    public String generateRefreshToken(UsersDetails usersDetails) {
        return build(usersDetails, refreshExpiracaoMs, true);
    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getIdFromToken(String token) {
        return getClaims(token).get(CLAIM_ID, Long.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Token JWT invalido: {}", e.getMessage());
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        return TIPO_REFRESH.equals(getClaims(token).get(CLAIM_TIPO, String.class));
    }

    public long getExpirationInSeconds() {
        return expirationsMs / 1000;
    }

    private String build(UsersDetails usersDetails, long expirationDateMs, boolean refresh) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + expirationDateMs);

        var builder = Jwts.builder()
                .subject(usersDetails.getUsername())
                .claim(CLAIM_ID, usersDetails.getId())
                .claim(CLAIM_PERFIL, usersDetails.getUser().getProfile().name())
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expirationTime)
                .signWith(getKey());

        if (refresh) {
            builder.claim(CLAIM_TIPO, TIPO_REFRESH);
        }
        return builder.compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
