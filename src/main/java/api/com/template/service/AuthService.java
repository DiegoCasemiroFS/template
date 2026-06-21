package api.com.template.service;

import api.com.template.domain.dto.input.LoginInput;
import api.com.template.domain.dto.input.UserInput;
import api.com.template.domain.dto.response.TokenResponse;
import api.com.template.domain.dto.response.UserResponse;
import api.com.template.domain.entity.User;
import api.com.template.exception.UnauthorizedException;
import api.com.template.security.TokenProvider;
import api.com.template.security.UsersDetails;
import api.com.template.security.UsersDetailsService;
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

    private final UserService service;
    private final UsersDetailsService usersDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Transactional
    public UserResponse register(UserInput input) {
        return UserResponse.from(service.save(input));
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginInput input) {
        
        User user = service.findByEmail(input.email());
        if (!user.isActive()) {
            throw new UnauthorizedException("Usuario nao esta ativo");
        }
        
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password()));

        return generateToken((UsersDetails) auth.getPrincipal());
    }

    @Transactional(readOnly = true)
    public TokenResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken) || !tokenProvider.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Refresh token invalido");
        }

        String email = tokenProvider.getEmailFromToken(refreshToken);
        UsersDetails user = (UsersDetails) usersDetailsService.loadUserByUsername(email);
        return generateToken(user);
    }

    private TokenResponse generateToken(UsersDetails user) {
        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);
        return TokenResponse.bearer(accessToken, refreshToken, tokenProvider.getExpirationInSeconds());
    }
}
