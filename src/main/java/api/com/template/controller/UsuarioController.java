package api.com.template.controller;

import api.com.template.domain.dto.input.UsuarioInput;
import api.com.template.domain.dto.response.UsuarioResponse;
import api.com.template.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CRUD de usuario. Todas as rotas exigem token; as de gestao (criar, listar,
 * atualizar, inativar) sao restritas a ADMIN. A listagem devolve Page direto.
 */
@Tag(name = "Usuarios")
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Retorna o usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> meusDados() {
        return ResponseEntity.ok(UsuarioResponse.de(usuarioService.getUsuarioLogado()));
    }

    @Operation(summary = "Busca um usuario por id")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.de(usuarioService.buscarPorId(id)));
    }

    @Operation(summary = "Lista usuarios paginados (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listar(pageable).map(UsuarioResponse::de));
    }

    @Operation(summary = "Atualiza um usuario (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id,
            @Valid @RequestBody UsuarioInput input) {
        return ResponseEntity.ok(UsuarioResponse.de(usuarioService.atualizar(id, input)));
    }

    @Operation(summary = "Inativa um usuario (somente ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
