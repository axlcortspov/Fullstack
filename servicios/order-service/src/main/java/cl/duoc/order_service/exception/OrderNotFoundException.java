package cl.duoc.order_service.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Order con id " + id + " no encontrada");
    }
}
