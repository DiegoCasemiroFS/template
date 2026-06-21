package api.com.template.domain.dto.response;

import api.com.template.domain.entity.Usuario;
import api.com.template.domain.enums.PerfilEnum;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Representacao de um usuario devolvida pela API (nunca expoe a senha). O
 * metodo de fabrica "de" converte a entidade neste record, evitando um mapper
 * separado.
 */
public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String cpf,
        LocalDate nascimento,
        PerfilEnum perfil,
        boolean ativo,
        Instant createdAt
        ) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getNascimento(),
                usuario.getPerfil(),
                usuario.isAtivo(),
                usuario.getCreatedAt()
        );
    }
}
