package cl.duoc.inventory_service.service;

import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<Inventory> findAll() {
        return repository.findAll();
    }

    public List<Inventory> findByProductId(Long productId) {
        return repository.findByProductId(productId);
    }

    public Inventory create(Inventory inventory) {
        return repository.save(inventory);
    }

    public Inventory update(Long id, Inventory inventoryData) {
        Inventory inventory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        inventory.setProductId(inventoryData.getProductId());
        inventory.setStock(inventoryData.getStock());
        inventory.setWarehouseLocation(inventoryData.getWarehouseLocation());

        return repository.save(inventory);
    }

    public Inventory updateStock(Long id, Integer stock) {
        Inventory inventory = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        inventory.setStock(stock);
        return repository.save(inventory);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}