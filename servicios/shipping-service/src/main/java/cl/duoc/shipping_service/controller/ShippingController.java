package cl.duoc.shipping_service.controller;

import cl.duoc.shipping_service.dto.ShippingResponseDTO;
import cl.duoc.shipping_service.model.Shipping;
import cl.duoc.shipping_service.service.ShippingService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final ShippingService service;

    public ShippingController(ShippingService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Shipping>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Shipping>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.findByOrderId(orderId));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ShippingResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithOrder(id));
    }

    @PostMapping
    public ResponseEntity<Shipping> create(@RequestBody Shipping shipping) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(shipping));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shipping> update(@PathVariable Long id, @RequestBody Shipping shipping) {
        return ResponseEntity.ok(service.update(id, shipping));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Shipping> updateStatus(
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