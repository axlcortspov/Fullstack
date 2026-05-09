package cl.duoc.cart_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("Usuario con id " + userId + " no encontrado en user-service");
    }
}
