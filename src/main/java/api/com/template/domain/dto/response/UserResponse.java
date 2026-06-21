package api.com.template.domain.dto.response;

import api.com.template.domain.entity.User;
import api.com.template.domain.enums.ProfileEnum;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Representacao de um usuario devolvida pela API (nunca expoe a senha). O
 * metodo de fabrica "de" converte a entidade neste record, evitando um mapper
 * separado.
 */
public record UserResponse(
        Long id,
        String name,
        String email,
        String cpf,
        LocalDate birth,
        ProfileEnum profile,
        boolean active,
        Instant createdAt
        ) {

    public static UserResponse from(User users) {
        return new UserResponse(
                users.getId(),
                users.getName(),
                users.getEmail(),
                users.getCpf(),
                users.getBirth(),
                users.getProfile(),
                users.isActive(),
                users.getCreatedAt()
        );
    }
}
