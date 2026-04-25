package cl.duoc.user_service.service;


import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.user_service.model.User;
import cl.duoc.user_service.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public User save(User user) {
        return repo.save(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
