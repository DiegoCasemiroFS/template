package api.com.template;

import api.com.template.domain.dto.input.LoginInput;
import api.com.template.domain.dto.input.UserInput;
import api.com.template.domain.entity.User;
import api.com.template.domain.enums.ProfileEnum;

import java.time.LocalDate;

/**
 * Fabrica de objetos genericos para os testes. Em vez de subir um banco, os
 * testes montam aqui as entidades e DTOs de exemplo que precisarem.
 */
public final class TestFixtures {

    private TestFixtures() {
    }

    public static User user() {
        return User.builder()
                .id(1L)
                .name("Maria Teste")
                .email("maria@teste.com")
                .password("$2a$10$hashFakeParaTestes")
                .cpf("52998224725")
                .birth(LocalDate.of(1990, 5, 20))
                .profile(ProfileEnum.USER)
                .active(true)
                .build();
    }

    public static UserInput userInput() {
        return new UserInput(
                "Maria Teste",
                "maria@teste.com",
                "senha123",
                "52998224725",
                LocalDate.of(1990, 5, 20));
    }

    public static LoginInput loginInput() {
        return new LoginInput("maria@teste.com", "senha123");
    }
}
