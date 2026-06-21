package api.com.template;

import api.com.template.domain.dto.input.LoginInput;
import api.com.template.domain.dto.input.UsuarioInput;
import api.com.template.domain.entity.Usuario;
import api.com.template.domain.enums.PerfilEnum;

import java.time.LocalDate;

/**
 * Fabrica de objetos genericos para os testes. Em vez de subir um banco, os
 * testes montam aqui as entidades e DTOs de exemplo que precisarem.
 */
public final class TestFixtures {

    private TestFixtures() {
    }

    public static Usuario usuario() {
        return Usuario.builder()
                .id(1L)
                .nome("Maria Teste")
                .email("maria@teste.com")
                .senha("$2a$10$hashFakeParaTestes")
                .cpf("52998224725")
                .nascimento(LocalDate.of(1990, 5, 20))
                .perfil(PerfilEnum.USER)
                .ativo(true)
                .build();
    }

    public static UsuarioInput usuarioInput() {
        return new UsuarioInput(
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
