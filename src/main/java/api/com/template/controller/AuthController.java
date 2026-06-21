package api.com.template.controller;

import api.com.template.domain.dto.input.LoginInput;
import api.com.template.domain.dto.input.RefreshTokenInput;
import api.com.template.domain.dto.input.UsuarioInput;
import api.com.template.domain.dto.response.TokenResponse;
import api.com.template.domain.dto.response.UsuarioResponse;
import api.com.template.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints publicos de autenticacao: cadastro, login e renovacao de token.
 */
@Tag(name = "Autenticacao")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Cadastra um novo usuario")
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(input));
    }

    @Operation(summary = "Autentica e devolve access e refresh token")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginInput input) {
        return ResponseEntity.ok(authService.login(input));
    }

    @Operation(summary = "Renova o par de tokens a partir do refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> renovar(@Valid @RequestBody RefreshTokenInput input) {
        return ResponseEntity.ok(authService.renovarToken(input.refreshToken()));
    }
}
