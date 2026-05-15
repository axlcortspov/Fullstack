package cl.duoc.payment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "El orderId es obligatorio")
    private Long orderId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double amount;

    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;
}
