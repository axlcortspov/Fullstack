package cl.duoc.user_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.user_service.model.User;
import cl.duoc.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Usuarios",
    description = "Operaciones CRUD para la gestión de usuarios"
)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Retorna una lista con todos los usuarios registrados"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Usuarios obtenidos correctamente"
    )
    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @Operation(
        summary = "Crear un usuario",
        description = "Registra un nuevo usuario en el sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario creado correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos"
        )
    })
    @PostMapping
    public User create(@RequestBody User user) {
        return service.save(user);
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario eliminado correctamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(
                description = "ID del usuario a eliminar",
                required = true
            )
            @PathVariable Long id) {

        service.delete(id);
    }

    @Operation(
        summary = "Buscar usuario por ID",
        description = "Obtiene la información de un usuario específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        )
    })
    @GetMapping("/{id}")
    public User getById(
            @Parameter(
                description = "ID único del usuario",
                required = true
            )
            @PathVariable Long id) {

        return service.findById(id);
    }
}