package api.com.template.domain.dto.input;

import jakarta.validation.constraints.NotBlank;

/**
 * Recebe o refresh token para emitir um novo par de tokens.
 */
public record RefreshTokenInput(
        @NotBlank(message = "{token.refresh.obrigatorio}")
        String refreshToken) {

}
