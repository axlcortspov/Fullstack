package cl.duoc.shipping_service.exception;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String serviceName) {
        super("El servicio '" + serviceName + "' no está disponible en este momento");
    }
}
