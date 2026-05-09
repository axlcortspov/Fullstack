package cl.duoc.shipping_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingResponseDTO {
    private Long id;
    private String address;
    private String status;
    private String trackingNumber;
    private OrderDTO order; 
}
