package cl.duoc.review_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
@Schema(name = "Review", description = "Modelo de reseña de producto")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la reseña", example = "1")
    private Long id;

    @Schema(description = "ID del usuario que realiza la reseña", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "ID del producto reseñado", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(description = "Calificación del producto (1-5)", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer rating;

    @Schema(description = "Comentario de la reseña", example = "Excelente producto, muy recomendado", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;

}