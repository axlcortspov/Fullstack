package cl.duoc.shipping_service.model;

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
@Table(name = "shippings")
@Schema(name = "Shipping", description = "Modelo de envío de orden")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del envío", example = "1")
    private Long id;

    @Schema(description = "ID de la orden a enviar", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderId;

    @Schema(description = "Dirección de envío", example = "Calle Principal 123, Apt 4B", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @Schema(description = "Estado del envío", example = "IN_TRANSIT")
    private String status;

    @Schema(description = "Número de seguimiento", example = "TRK123456789")
    private String trackingNumber;

}
