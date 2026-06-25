package cl.duoc.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderResponseDTO {
    private Long id;
    private String status;
    private Double total;
    private UserDTO user;   
}
