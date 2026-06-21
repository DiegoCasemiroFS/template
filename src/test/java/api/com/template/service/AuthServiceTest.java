package api.com.template.service;

import api.com.template.TestFixtures;
import api.com.template.domain.dto.input.LoginInput;
import api.com.template.domain.dto.response.TokenResponse;
import api.com.template.security.TokenProvider;
import api.com.template.security.UsersDetails;
import api.com.template.security.UsersDetailsService;
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

    @Mock private UserService service;
    @Mock private UsersDetailsService usersDetailsService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenProvider tokenProvider;
    @Mock private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldGenerateTokensOnLogin() {
        LoginInput input = TestFixtures.loginInput();
        UsersDetails details = new UsersDetails(TestFixtures.user());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(details);
        when(tokenProvider.generateAccessToken(details)).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken(details)).thenReturn("refresh-token");
        when(tokenProvider.getExpirationInSeconds()).thenReturn(86400L);
        when(service.findByEmail(input.email())).thenReturn(TestFixtures.user());

        TokenResponse response = authService.login(input);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.type()).isEqualTo("Bearer");
    }
}
