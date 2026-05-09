package cl.duoc.inventory_service.exception;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(Long id) {
        super("Inventario con id " + id + " no encontrado");
    }
}