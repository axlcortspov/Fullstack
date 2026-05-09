package cl.duoc.notification_service.exception;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(Long id) {
        super("Notificación con id " + id + " no encontrada");
    }
}