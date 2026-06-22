package api.com.template.controller;

import api.com.template.domain.dto.input.UserInput;
import api.com.template.domain.dto.response.UserResponse;
import api.com.template.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CRUD de usuario. Todas as rotas exigem token; as de gestao (criar, listar,
 * atualizar, inativar) sao restritas a ADMIN. A listagem devolve Page direto.
 */
@Tag(name = "User")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Retorna o usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(UserResponse.from(service.getUserLogged()));
    }

    @Operation(summary = "Busca um usuario por id")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.from(service.findById(id)));
    }
    
    @Operation(summary = "Busca um usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(UserResponse.from(service.findByEmail(email)));
    }

    @Operation(summary = "Lista usuarios paginados (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable).map(UserResponse::from));
    }

    @Operation(summary = "Atualiza um usuario (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
            @Valid @RequestBody UserInput input) {
        return ResponseEntity.ok(UserResponse.from(service.atualizar(id, input)));
    }

    @Operation(summary = "Inativa um usuario (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
