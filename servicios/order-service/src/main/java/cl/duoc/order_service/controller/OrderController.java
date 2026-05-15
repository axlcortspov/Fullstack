package cl.duoc.order_service.controller;

import cl.duoc.order_service.dto.OrderRequestDTO;
import cl.duoc.order_service.dto.OrderResponseDTO;
import cl.duoc.order_service.model.Order;
import cl.duoc.order_service.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<OrderResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithUser(id));
    }
    
    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}