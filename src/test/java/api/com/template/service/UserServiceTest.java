package api.com.template.service;

import api.com.template.TestFixtures;
import api.com.template.domain.entity.User;
import api.com.template.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import api.com.template.repository.UserRepository;

/**
 * Testes de unidade do UsuarioService com Mockito. O repositorio e mockado, entao
 * nenhum banco e necessario; os dados vem das fixtures.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ModelMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void shouldSearchUserByIdWhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(TestFixtures.user()));

        User result = service.findById(1L);

        assertThat(result.getEmail()).isEqualTo("maria@teste.com");
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(repository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(123L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("123");
    }
}
