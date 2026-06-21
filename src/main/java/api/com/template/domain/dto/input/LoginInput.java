package api.com.template.domain.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Credenciais recebidas no endpoint de login.
 */
public record LoginInput(
        
        @NotBlank(message = "{usuario.email.obrigatorio}")
        @Email(message = "{usuario.email.invalido}")
        String email,
        
        @NotBlank(message = "{usuario.senha.obrigatorio}")
        String senha) {

}
