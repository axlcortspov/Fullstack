package cl.duoc.auth_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.auth_service.model.AuthUser;
import cl.duoc.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API de autenticación de usuarios")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthUser.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public AuthUser register(@RequestBody AuthUser user) {
        return service.save(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y retorna un token")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
        content = @Content(mediaType = "application/json", schema = @Schema(type = "string")))
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public String login(@RequestBody AuthUser user) {
        return service.login(user);
    }
}
