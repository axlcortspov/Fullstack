package cl.duoc.shipping_service.exception;

public class ShippingNotFoundException extends RuntimeException {
    public ShippingNotFoundException(Long id) {
        super("Envío con id " + id + " no encontrado");
    }
}
