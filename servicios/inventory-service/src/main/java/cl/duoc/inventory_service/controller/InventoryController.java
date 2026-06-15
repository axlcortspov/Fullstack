package cl.duoc.inventory_service.controller;

import cl.duoc.inventory_service.dto.InventoryResponseDTO;
import cl.duoc.inventory_service.model.Inventory;
import cl.duoc.inventory_service.service.InventoryService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory", description = "API de inventario de productos")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todo el inventario", description = "Retorna todos los items del inventario")
    @ApiResponse(responseCode = "200", description = "Inventario obtenido exitosamente")
    public ResponseEntity<List<Inventory>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Obtener inventario por producto", description = "Retorna el inventario de un producto específico")
    @ApiResponse(responseCode = "200", description = "Inventario del producto obtenido")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<List<Inventory>> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(service.findByProductId(productId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Obtener detalle de inventario", description = "Retorna los detalles del inventario incluyendo datos del producto")
    @ApiResponse(responseCode = "200", description = "Detalle obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    public ResponseEntity<InventoryResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithProduct(id));
    }

    @PostMapping
    @Operation(summary = "Crear inventario", description = "Crea un nuevo registro de inventario")
    @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventory.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<Inventory> create(@RequestBody Inventory inventory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(inventory));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inventario", description = "Actualiza un registro de inventario")
    @ApiResponse(responseCode = "200", description = "Inventario actualizado")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    public ResponseEntity<Inventory> update(
            @PathVariable Long id,
            @RequestBody Inventory inventory
    ) {
        return ResponseEntity.ok(service.update(id, inventory));
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock", description = "Actualiza la cantidad de stock de un inventario")
    @ApiResponse(responseCode = "200", description = "Stock actualizado")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    public ResponseEntity<Inventory> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock
    ) {
        return ResponseEntity.ok(service.updateStock(id, stock));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inventario", description = "Elimina un registro de inventario")
    @ApiResponse(responseCode = "204", description = "Inventario eliminado")
    @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}