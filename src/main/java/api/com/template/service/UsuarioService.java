package api.com.template.service;

import api.com.template.domain.dto.input.UsuarioInput;
import api.com.template.domain.entity.Usuario;
import api.com.template.domain.enums.PerfilEnum;
import api.com.template.exception.NaoAutorizadoException;
import api.com.template.exception.NegocioException;
import api.com.template.exception.RecursoNaoEncontradoException;
import api.com.template.repository.UsuarioRepository;
import api.com.template.security.UsuarioDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de negocio de usuario: criar, buscar, listar, atualizar e inativar,
 * alem de recuperar o usuario autenticado (getUsuarioLogado) a partir do
 * contexto de seguranca populado pelo filtro JWT.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> RecursoNaoEncontradoException.de("Usuario", id));
    }

    @Transactional(readOnly = true)
    public Page<Usuario> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public Usuario criar(UsuarioInput input) {
        if (repository.existsByEmail(input.email())) {
            throw new NegocioException("Email ja cadastrado: " + input.email());
        }
        if (repository.existsByCpf(input.cpf())) {
            throw new NegocioException("CPF ja cadastrado: " + input.cpf());
        }

        Usuario usuario = mapper.map(input, Usuario.class);
        usuario.setSenha(passwordEncoder.encode(input.senha()));
        usuario.setPerfil(PerfilEnum.USER);
        usuario.setAtivo(true);

        return repository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioInput input) {
        Usuario usuario = buscarPorId(id);

        if (input.email() != null && !input.email().equals(usuario.getEmail())
                && repository.existsByEmail(input.email())) {
            throw new NegocioException("Email ja cadastrado: " + input.email());
        }

        mapper.map(input, usuario);
        usuario.setSenha(passwordEncoder.encode(input.senha()));
        usuario.setAtivo(true);

        return repository.save(usuario);
    }

    @Transactional
    public void inativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UsuarioDetails detalhes)) {
            throw new NaoAutorizadoException("Nenhum usuario autenticado");
        }
        return buscarPorId(detalhes.getId());
    }
}
