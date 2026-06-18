package cl.duoc.user_service.controller;

import cl.duoc.user_service.model.User;
import cl.duoc.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    private UserService service;

    @BeforeEach
    void setup() {
        service = Mockito.mock(UserService.class);

        UserController controller =
                new UserController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnUsers() throws Exception {

        when(service.getAll()).thenReturn(
                List.of(
                        new User(
                                1L,
                                "Juan",
                                "juan@gmail.com"
                        )
                )
        );

        mockMvc.perform(
                get("/api/v1/users")
        )
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateUser() throws Exception {

        User user = new User(
                1L,
                "Juan",
                "juan@gmail.com"
        );

        when(service.save(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(
                post("/api/v1/users")
                        .contentType("application/json")
                        .content("""
                            {
                              "name":"Juan",
                              "email":"juan@gmail.com"
                            }
                        """)
        )
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUserById() throws Exception {

        when(service.findById(1L))
                .thenReturn(
                        new User(
                                1L,
                                "Juan",
                                "juan@gmail.com"
                        )
                );

        mockMvc.perform(
                get("/api/v1/users/1")
        )
                .andExpect(status().isOk());
    }
}