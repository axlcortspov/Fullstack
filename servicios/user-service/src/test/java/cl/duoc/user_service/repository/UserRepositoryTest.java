package cl.duoc.user_service.repository;

import cl.duoc.user_service.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldSaveUser() {

        User user = new User(
                null,
                "Juan Perez",
                "juan@gmail.com"
        );

        User saved = repository.save(user);

        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindUserById() {

        User saved = repository.save(
                new User(
                        null,
                        "Maria Soto",
                        "maria@gmail.com"
                )
        );

        Optional<User> result =
                repository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(
                "Maria Soto",
                result.get().getName()
        );
    }

    @Test
    void shouldFindAllUsers() {

        repository.save(
                new User(
                        null,
                        "User 1",
                        "u1@gmail.com"
                )
        );

        repository.save(
                new User(
                        null,
                        "User 2",
                        "u2@gmail.com"
                )
        );

        List<User> users =
                repository.findAll();

        assertTrue(users.size() >= 2);
    }
}