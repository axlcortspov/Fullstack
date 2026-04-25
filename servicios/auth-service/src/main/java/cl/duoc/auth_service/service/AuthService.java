package cl.duoc.auth_service.service;

import org.springframework.stereotype.Service;

import cl.duoc.auth_service.model.AuthUser;
import cl.duoc.auth_service.repository.AuthRepository;

@Service
public class AuthService {

    private final AuthRepository repo;

    public AuthService(AuthRepository repo) {
        this.repo = repo;
    }

    public AuthUser save(AuthUser user) {
        return repo.save(user);
    }

    public String login(AuthUser user) {
        AuthUser found = repo.findByUsername(user.getUsername());
        if (found != null && found.getPassword().equals(user.getPassword())) {
            return "Login exitoso";
        }
        return "Credenciales inválidas";
    }
}
