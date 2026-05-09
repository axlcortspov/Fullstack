package cl.duoc.inventory_service.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long productId) {
        super("Producto con id " + productId + " no encontrado en product-service");
    }
}