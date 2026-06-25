package cl.duoc.payment_service.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderDTO {
    private Long id;
    private Long userId;
    private Double total;
    private String status;
}
