package api.com.template.domain.dto.response;

/**
 * Resposta de autenticacao: o access token (curta duracao) e o refresh token
 * (longa duracao), alem do tipo e do tempo de expiracao do access em segundos.
 */
public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tipo,
        long expiraEm
        ) {

    public static TokenResponse bearer(String accessToken, String refreshToken, long expiraEm) {
        return new TokenResponse(accessToken, refreshToken, "Bearer", expiraEm);
    }
}
