package cl.duoc.product_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@Entity
@Schema(name = "Product", description = "Modelo de producto")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Laptop Dell", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Precio del producto", example = "999.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private double price;
}