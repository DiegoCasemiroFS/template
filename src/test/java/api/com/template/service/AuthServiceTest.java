package api.com.template.service;

import api.com.template.TestFixtures;
import api.com.template.domain.dto.input.LoginInput;
import api.com.template.domain.dto.response.TokenResponse;
import api.com.template.security.TokenProvider;
import api.com.template.security.UsuarioDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Testes de unidade do AuthService. Todas as dependencias sao mockadas; o foco e
 * o fluxo de login devolvendo o par de tokens.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UsuarioService service;
    @Mock private api.com.template.security.UsuarioDetailsService usuarioDetailsService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenProvider tokenProvider;
    @Mock private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveGerarTokensNoLogin() {
        LoginInput input = TestFixtures.loginInput();
        UsuarioDetails details = new UsuarioDetails(TestFixtures.usuario());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(details);
        when(tokenProvider.gerarAccessToken(details)).thenReturn("access-token");
        when(tokenProvider.gerarRefreshToken(details)).thenReturn("refresh-token");
        when(tokenProvider.getExpiracaoEmSegundos()).thenReturn(86400L);

        TokenResponse resposta = authService.login(input);

        assertThat(resposta.accessToken()).isEqualTo("access-token");
        assertThat(resposta.refreshToken()).isEqualTo("refresh-token");
        assertThat(resposta.tipo()).isEqualTo("Bearer");
    }
}
