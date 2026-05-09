package cl.duoc.review_service.controller;

import cl.duoc.review_service.dto.ReviewResponseDTO;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.service.ReviewService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(service.findByProductId(productId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ReviewResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithDetails(id));
    }

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(review));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(
            @PathVariable Long id,
            @RequestBody Review review
    ) {
        return ResponseEntity.ok(service.update(id, review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}