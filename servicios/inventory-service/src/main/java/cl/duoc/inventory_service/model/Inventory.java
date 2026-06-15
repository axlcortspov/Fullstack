package cl.duoc.inventory_service.model;

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
@Table(name = "inventory")
@Schema(name = "Inventory", description = "Modelo de inventario de productos")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del inventario", example = "1")
    private Long id;

    @Schema(description = "ID del producto", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @Schema(description = "Cantidad de stock disponible", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer stock;

    @Schema(description = "Ubicación del almacén", example = "A-3-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private String warehouseLocation;
}