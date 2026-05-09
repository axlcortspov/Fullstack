package cl.duoc.payment_service.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("Orden con id " + orderId + " no encontrada en order-service");
    }
}
