package cl.duoc.review_service.controller;

import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<Review> getAll() {
        return service.findAll();
    }

    @GetMapping("/product/{productId}")
    public List<Review> getByProductId(@PathVariable Long productId) {
        return service.findByProductId(productId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> getByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @PostMapping
    public Review create(@RequestBody Review review) {
        return service.create(review);
    }

    @PutMapping("/{id}")
    public Review update(@PathVariable Long id, @RequestBody Review review) {
        return service.update(id, review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}