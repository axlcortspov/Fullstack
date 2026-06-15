package cl.duoc.payment_service.model;

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

@Table(name = "payments")
@Schema(name = "Payment", description = "Modelo de pago de orden")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pago", example = "1")
    private Long id;

    @Schema(description = "ID de la orden asociada", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderId;

    @Schema(description = "Monto del pago", example = "99.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double amount;

    @Schema(description = "Método de pago utilizado", example = "CREDIT_CARD", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentMethod;

    @Schema(description = "Estado del pago", example = "COMPLETED")
    private String status;

}
