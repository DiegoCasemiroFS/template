package api.com.template.domain.dto.response;

/**
 * Resposta de autenticacao: o access token (curta duracao) e o refresh token
 * (longa duracao), alem do tipo e do tempo de expiracao do access em segundos.
 */
public record TokenResponse(
        String accessToken,
        String refreshToken,
        String type,
        long expirationTime
        ) {

    public static TokenResponse bearer(String accessToken, String refreshToken, long expirationTime) {
        return new TokenResponse(accessToken, refreshToken, "Bearer", expirationTime);
    }
}
