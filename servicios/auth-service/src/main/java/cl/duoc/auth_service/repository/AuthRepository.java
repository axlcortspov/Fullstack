package cl.duoc.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.auth_service.model.AuthUser;

public interface AuthRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByUsername(String username);
}
