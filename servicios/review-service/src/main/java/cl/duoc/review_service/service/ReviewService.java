package cl.duoc.review_service.service;

import cl.duoc.review_service.client.ProductClient;
import cl.duoc.review_service.client.UserClient;
import cl.duoc.review_service.dto.ProductDTO;
import cl.duoc.review_service.dto.ReviewResponseDTO;
import cl.duoc.review_service.dto.UserDTO;
import cl.duoc.review_service.exception.ProductNotFoundException;
import cl.duoc.review_service.exception.ReviewNotFoundException;
import cl.duoc.review_service.exception.ServiceUnavailableException;
import cl.duoc.review_service.exception.UserNotFoundException;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.repository.ReviewRepository;
import feign.FeignException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public ReviewService(
            ReviewRepository repository,
            UserClient userClient,
            ProductClient productClient
    ) {
        this.repository = repository;
        this.userClient = userClient;
        this.productClient = productClient;
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public List<Review> findByProductId(Long productId) {
        validateProduct(productId);
        return repository.findByProductId(productId);
    }

    public List<Review> findByUserId(Long userId) {
        validateUser(userId);
        return repository.findByUserId(userId);
    }

    public Review create(Review review) {
        log.info("[review-service] Validando usuario id={} y producto id={}",
                review.getUserId(), review.getProductId());

        UserDTO user = validateUser(review.getUserId());
        ProductDTO product = validateProduct(review.getProductId());

        Review saved = repository.save(review);

        log.info("[review-service] Reseña creada con id={} para usuario={} y producto={}",
                saved.getId(), user.getName(), product.getName());

        return saved;
    }

    public ReviewResponseDTO findByIdWithDetails(Long id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        log.info("[review-service] Obteniendo detalles para reseña id={}", id);

        UserDTO user = validateUser(review.getUserId());
        ProductDTO product = validateProduct(review.getProductId());

        log.info("[review-service] Detalles obtenidos para reseña id={}", id);

        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                user,
                product
        );
    }

    public Review update(Long id, Review reviewData) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));

        validateUser(reviewData.getUserId());
        validateProduct(reviewData.getProductId());

        review.setUserId(reviewData.getUserId());
        review.setProductId(reviewData.getProductId());
        review.setRating(reviewData.getRating());
        review.setComment(reviewData.getComment());

        log.info("[review-service] Reseña id={} actualizada", id);

        return repository.save(review);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ReviewNotFoundException(id);
        }

        repository.deleteById(id);

        log.info("[review-service] Reseña id={} eliminada", id);
    }

    private UserDTO validateUser(Long userId) {
        try {
            log.info("[review-service] Consultando user-service para usuario id={}", userId);

            UserDTO user = userClient.getUserById(userId);

            log.info("[review-service] Usuario encontrado: id={}, name={}",
                    user.getId(), user.getName());

            return user;

        } catch (FeignException.NotFound ex) {
            log.warn("[review-service] Usuario id={} no encontrado en user-service", userId);
            throw new UserNotFoundException(userId);

        } catch (FeignException ex) {
            log.error("[review-service] user-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("user-service");
        }
    }

    private ProductDTO validateProduct(Long productId) {
        try {
            log.info("[review-service] Consultando product-service para producto id={}", productId);

            ProductDTO product = productClient.getProductById(productId);

            log.info("[review-service] Producto encontrado: id={}, name={}",
                    product.getId(), product.getName());

            return product;

        } catch (FeignException.NotFound ex) {
            log.warn("[review-service] Producto id={} no encontrado en product-service", productId);
            throw new ProductNotFoundException(productId);

        } catch (FeignException ex) {
            log.error("[review-service] product-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("product-service");
        }
    }
}