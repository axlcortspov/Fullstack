package cl.duoc.cart_service.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long id) {
        super("CartItem con id " + id + " no encontrado");
    }
}
