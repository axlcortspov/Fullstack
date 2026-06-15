package cl.duoc.order_service.model;

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
@Table(name = "orders")
@Schema(name = "Order", description = "Modelo de orden de compra")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la orden", example = "1")
    private Long id;

    @Schema(description = "ID del usuario que realiza la orden", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "Monto total de la orden", example = "299.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double total;

    @Schema(description = "Estado actual de la orden", example = "PENDING")
    private String status;
}
