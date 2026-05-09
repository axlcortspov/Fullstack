package cl.duoc.inventory_service.service;

import cl.duoc.inventory_service.client.ProductClient;
import cl.duoc.inventory_service.dto.InventoryResponseDTO;
import cl.duoc.inventory_service.dto.ProductDTO;
import cl.duoc.inventory_service.exception.InventoryNotFoundException;
import cl.duoc.inventory_service.exception.ProductNotFoundException;
import cl.duoc.inventory_service.exception.ServiceUnavailableException;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.repository.InventoryRepository;
import feign.FeignException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InventoryService {

    private final InventoryRepository repository;
    private final ProductClient productClient;

    public InventoryService(InventoryRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public List<Inventory> findAll() {
        return repository.findAll();
    }

    public List<Inventory> findByProductId(Long productId) {
        validateProduct(productId);
        return repository.findByProductId(productId);
    }

    public Inventory create(Inventory inventory) {
        log.info("[inventory-service] Validando producto id={} en product-service", inventory.getProductId());

        ProductDTO product = validateProduct(inventory.getProductId());

        Inventory saved = repository.save(inventory);

        log.info("[inventory-service] Inventario creado con id={} para producto={}",
                saved.getId(), product.getName());

        return saved;
    }

    public InventoryResponseDTO findByIdWithProduct(Long id) {
        Inventory inventory = repository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        log.info("[inventory-service] Obteniendo producto id={} para inventario id={}",
                inventory.getProductId(), id);

        ProductDTO product = validateProduct(inventory.getProductId());

        log.info("[inventory-service] Producto obtenido para inventario id={}", id);

        return new InventoryResponseDTO(
                inventory.getId(),
                inventory.getStock(),
                inventory.getWarehouseLocation(),
                product
        );
    }

    public Inventory update(Long id, Inventory inventoryData) {
        Inventory inventory = repository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        validateProduct(inventoryData.getProductId());

        inventory.setProductId(inventoryData.getProductId());
        inventory.setStock(inventoryData.getStock());
        inventory.setWarehouseLocation(inventoryData.getWarehouseLocation());

        log.info("[inventory-service] Inventario id={} actualizado", id);

        return repository.save(inventory);
    }

    public Inventory updateStock(Long id, Integer stock) {
        Inventory inventory = repository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException(id));

        inventory.setStock(stock);

        log.info("[inventory-service] Stock de inventario id={} actualizado a {}", id, stock);

        return repository.save(inventory);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new InventoryNotFoundException(id);
        }

        repository.deleteById(id);

        log.info("[inventory-service] Inventario id={} eliminado", id);
    }

    private ProductDTO validateProduct(Long productId) {
        try {
            log.info("[inventory-service] Consultando product-service para producto id={}", productId);

            ProductDTO product = productClient.getProductById(productId);

            log.info("[inventory-service] Producto encontrado: id={}, name={}",
                    product.getId(), product.getName());

            return product;

        } catch (FeignException.NotFound ex) {
            log.warn("[inventory-service] Producto id={} no encontrado en product-service", productId);
            throw new ProductNotFoundException(productId);

        } catch (FeignException ex) {
            log.error("[inventory-service] product-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("product-service");
        }
    }
}