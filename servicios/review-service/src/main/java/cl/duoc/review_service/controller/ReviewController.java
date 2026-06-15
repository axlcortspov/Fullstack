package cl.duoc.review_service.controller;

import cl.duoc.review_service.dto.ReviewResponseDTO;
import cl.duoc.review_service.model.Review;
import cl.duoc.review_service.service.ReviewService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/reviews")
@Tag(name = "Reviews", description = "API de reseñas de productos")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las reseñas", description = "Retorna todas las reseñas de productos")
    @ApiResponse(responseCode = "200", description = "Reseñas obtenidas exitosamente")
    public ResponseEntity<List<Review>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Obtener reseñas de producto", description = "Retorna todas las reseñas de un producto específico")
    @ApiResponse(responseCode = "200", description = "Reseñas obtenidas")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<List<Review>> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(service.findByProductId(productId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener reseñas de usuario", description = "Retorna todas las reseñas realizadas por un usuario")
    @ApiResponse(responseCode = "200", description = "Reseñas obtenidas")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<List<Review>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Obtener detalle de reseña", description = "Retorna los detalles completos de una reseña")
    @ApiResponse(responseCode = "200", description = "Detalle obtenido")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    public ResponseEntity<ReviewResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithDetails(id));
    }

    @PostMapping
    @Operation(summary = "Crear reseña", description = "Crea una nueva reseña de producto")
    @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<Review> create(@RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(review));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reseña", description = "Actualiza una reseña existente")
    @ApiResponse(responseCode = "200", description = "Reseña actualizada")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    public ResponseEntity<Review> update(
            @PathVariable Long id,
            @RequestBody Review review
    ) {
        return ResponseEntity.ok(service.update(id, review));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña de producto")
    @ApiResponse(responseCode = "204", description = "Reseña eliminada")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}