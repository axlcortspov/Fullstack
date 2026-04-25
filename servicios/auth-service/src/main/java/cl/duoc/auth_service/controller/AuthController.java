package cl.duoc.auth_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.auth_service.model.AuthUser;
import cl.duoc.auth_service.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public AuthUser register(@RequestBody AuthUser user) {
        return service.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthUser user) {
        return service.login(user);
    }
}
