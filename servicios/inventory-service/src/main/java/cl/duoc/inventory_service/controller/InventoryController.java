package cl.duoc.inventory_service.controller;

import cl.duoc.inventory_service.dto.InventoryResponseDTO;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.service.InventoryService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Inventory>> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(service.findByProductId(productId));
    }

    // Endpoint enriquecido: inventario con datos del producto vía Feign
    @GetMapping("/{id}/detail")
    public ResponseEntity<InventoryResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithProduct(id));
    }

    // Al crear, valida que el producto exista en product-service
    @PostMapping
    public ResponseEntity<Inventory> create(@RequestBody Inventory inventory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(inventory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(
            @PathVariable Long id,
            @RequestBody Inventory inventory
    ) {
        return ResponseEntity.ok(service.update(id, inventory));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Inventory> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock
    ) {
        return ResponseEntity.ok(service.updateStock(id, stock));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}