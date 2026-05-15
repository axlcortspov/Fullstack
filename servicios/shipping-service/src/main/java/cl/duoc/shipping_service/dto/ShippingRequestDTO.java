package cl.duoc.shipping_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShippingRequestDTO {

    @NotNull(message = "El orderId es obligatorio")
    private Long orderId;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;
}
