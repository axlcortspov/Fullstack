package cl.duoc.review_service.exception;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(Long id) {
        super("Reseña con id " + id + " no encontrada");
    }
}