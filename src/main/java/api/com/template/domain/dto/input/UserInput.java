package api.com.template.domain.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

/**
 * Dados recebidos para cadastrar um usuario. Validados pelo @Valid no
 * controller antes de chegar ao service.
 */
public record UserInput(
        @NotBlank(message = "{usuario.nome.obrigatorio}")
        String name,

        @NotBlank(message = "{usuario.email.obrigatorio}")
        @Email(message = "{usuario.email.invalido}")
        String email,

        @NotBlank(message = "{usuario.senha.obrigatorio}")
        @Size(min = 6, message = "{usuario.senha.tamanho}")
        String password,

        @NotBlank(message = "{usuario.cpf.obrigatorio}")
        @CPF(message = "{usuario.cpf.invalido}")
        String cpf,

        @NotNull(message = "{usuario.nascimento.obrigatorio}")
        @Past(message = "{usuario.nascimento.passado}")
        LocalDate birth) {

}
