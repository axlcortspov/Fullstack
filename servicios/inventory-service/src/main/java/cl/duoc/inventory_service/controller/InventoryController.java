package cl.duoc.inventory_service.controller;

import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Inventory> getAll() {
        return service.findAll();
    }

    @GetMapping("/product/{productId}")
    public List<Inventory> getByProductId(@PathVariable Long productId) {
        return service.findByProductId(productId);
    }

    @PostMapping
    public Inventory create(@RequestBody Inventory inventory) {
        return service.create(inventory);
    }

    @PutMapping("/{id}")
    public Inventory update(@PathVariable Long id, @RequestBody Inventory inventory) {
        return service.update(id, inventory);
    }

    @PutMapping("/{id}/stock")
    public Inventory updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        return service.updateStock(id, stock);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}