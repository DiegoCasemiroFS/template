package api.com.template.service;

import api.com.template.TestFixtures;
import api.com.template.domain.entity.Usuario;
import api.com.template.exception.RecursoNaoEncontradoException;
import api.com.template.repository.UsuarioRepository;
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

/**
 * Testes de unidade do UsuarioService com Mockito. O repositorio e mockado, entao
 * nenhum banco e necessario; os dados vem das fixtures.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository repository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ModelMapper mapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveBuscarUsuarioPorIdQuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(TestFixtures.usuario()));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertThat(resultado.getEmail()).isEqualTo("maria@teste.com");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        when(repository.findById(123L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(123L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("123");
    }
}
