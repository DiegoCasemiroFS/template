package api.com.template.service;

import api.com.template.domain.dto.input.LoginInput;
import api.com.template.domain.dto.input.UsuarioInput;
import api.com.template.domain.dto.response.TokenResponse;
import api.com.template.domain.dto.response.UsuarioResponse;
import api.com.template.exception.NaoAutorizadoException;
import api.com.template.security.TokenProvider;
import api.com.template.security.UsuarioDetails;
import api.com.template.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de autenticacao: cadastro publico (sempre como USER), login com
 * geracao de access e refresh token, e renovacao do par de tokens via refresh
 * token.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioService service;
    private final UsuarioDetailsService usuarioDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    public UsuarioResponse registrar(UsuarioInput input) {
        return UsuarioResponse.de(service.criar(input));
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginInput input) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.senha()));

        return gerarTokens((UsuarioDetails) auth.getPrincipal());
    }

    @Transactional(readOnly = true)
    public TokenResponse renovarToken(String refreshToken) {
        if (!tokenProvider.validarToken(refreshToken) || !tokenProvider.ehRefreshToken(refreshToken)) {
            throw new NaoAutorizadoException("Refresh token invalido");
        }

        String email = tokenProvider.getEmailDoToken(refreshToken);
        UsuarioDetails usuario = (UsuarioDetails) usuarioDetailsService.loadUserByUsername(email);
        return gerarTokens(usuario);
    }

    private TokenResponse gerarTokens(UsuarioDetails usuario) {
        String accessToken = tokenProvider.gerarAccessToken(usuario);
        String refreshToken = tokenProvider.gerarRefreshToken(usuario);
        return TokenResponse.bearer(accessToken, refreshToken, tokenProvider.getExpiracaoEmSegundos());
    }
}
