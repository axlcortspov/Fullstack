package cl.duoc.user_service.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void shouldCreateEmptyUser() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void shouldCreateUserWithAllArgsConstructor() {
        User user = new User(
                1L,
                "Juan Perez",
                "juan@gmail.com"
        );

        assertEquals(1L, user.getId());
        assertEquals("Juan Perez", user.getName());
        assertEquals("juan@gmail.com", user.getEmail());
    }

    @Test
    void shouldSetAndGetValues() {
        User user = new User();

        user.setId(1L);
        user.setName("Maria Soto");
        user.setEmail("maria@gmail.com");

        assertEquals(1L, user.getId());
        assertEquals("Maria Soto", user.getName());
        assertEquals("maria@gmail.com", user.getEmail());
    }
}