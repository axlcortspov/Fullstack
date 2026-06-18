package cl.duoc.user_service.service;

import cl.duoc.user_service.model.User;
import cl.duoc.user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(
                new User(1L, "Juan Pérez", "juan@gmail.com"),
                new User(2L, "María Soto", "maria@gmail.com")
        );

        when(repo.findAll()).thenReturn(users);

        List<User> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Juan Pérez", result.get(0).getName());

        verify(repo, times(1)).findAll();
    }

    @Test
    void shouldSaveUser() {
        User user = new User(
                1L,
                "Axel Cortes",
                "axel@gmail.com"
        );

        when(repo.save(any(User.class))).thenReturn(user);

        User result = service.save(user);

        assertNotNull(result);
        assertEquals("Axel Cortes", result.getName());
        assertEquals("axel@gmail.com", result.getEmail());

        verify(repo, times(1)).save(user);
    }

    @Test
    void shouldFindUserById() {
        User user = new User(
                1L,
                "Juan Pérez",
                "juan@gmail.com"
        );

        when(repo.findById(1L))
                .thenReturn(Optional.of(user));

        User result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Juan Pérez", result.getName());

        verify(repo, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(repo.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findById(99L)
        );

        assertEquals(
                "Usuario no encontrado",
                exception.getMessage()
        );
    }

    @Test
    void shouldDeleteUser() {
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}