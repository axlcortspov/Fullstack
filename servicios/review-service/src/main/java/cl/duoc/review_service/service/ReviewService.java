package cl.duoc.review_service.service;

import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public List<Review> findByProductId(Long productId) {
        return repository.findByProductId(productId);
    }

    public List<Review> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Review create(Review review) {
        return repository.save(review);
    }

    public Review update(Long id, Review reviewData) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        review.setUserId(reviewData.getUserId());
        review.setProductId(reviewData.getProductId());
        review.setRating(reviewData.getRating());
        review.setComment(reviewData.getComment());

        return repository.save(review);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}