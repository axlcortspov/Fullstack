package cl.duoc.cart_service.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super("Producto con id " + productId + " no encontrado en product-service");
    }
}
