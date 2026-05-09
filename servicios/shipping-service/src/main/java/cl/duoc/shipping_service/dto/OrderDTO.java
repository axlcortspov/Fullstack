package cl.duoc.shipping_service.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private Double total;
    private String status;
}
