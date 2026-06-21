package api.com.template.service;

import api.com.template.domain.dto.input.UserInput;
import api.com.template.domain.entity.User;
import api.com.template.domain.enums.ProfileEnum;
import api.com.template.exception.UnauthorizedException;
import api.com.template.exception.BusinessException;
import api.com.template.exception.ResourceNotFoundException;
import api.com.template.security.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import api.com.template.repository.UserRepository;

/**
 * Regras de negocio de usuario: criar, buscar, listar, atualizar e inativar,
 * alem de recuperar o usuario autenticado (getUsuarioLogado) a partir do
 * contexto de seguranca populado pelo filtro JWT.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.from("Usuario", id));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> ResourceNotFoundException.from("Usuario", email));
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public User save(UserInput input) {
        if (repository.existsByEmail(input.email())) {
            throw new BusinessException("Email ja cadastrado: " + input.email());
        }
        if (repository.existsByCpf(input.cpf())) {
            throw new BusinessException("CPF ja cadastrado: " + input.cpf());
        }

        User user = mapper.map(input, User.class);
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setProfile(ProfileEnum.USER);
        user.setActive(true);

        return repository.save(user);
    }

    @Transactional
    public User atualizar(Long id, UserInput input) {
        User user = findById(id);

        if (input.email() != null && !input.email().equals(user.getEmail())
                && repository.existsByEmail(input.email())) {
            throw new BusinessException("Email ja cadastrado: " + input.email());
        }

        mapper.map(input, user);
        user.setPassword(passwordEncoder.encode(input.password()));
        user.setActive(true);

        return repository.save(user);
    }

    @Transactional
    public void inativar(Long id) {
        User user = findById(id);
        user.setActive(false);
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserLogged() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UsersDetails detalhes)) {
            throw new UnauthorizedException("Nenhum usuario autenticado");
        }
        return findById(detalhes.getId());
    }
}
