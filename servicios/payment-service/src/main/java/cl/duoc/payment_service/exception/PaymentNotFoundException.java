package cl.duoc.payment_service.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Pago con id " + id + " no encontrado");
    }
}
