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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders", description = "API de gestión de órdenes")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Retorna todas las órdenes registradas")
    @ApiResponse(responseCode = "200", description = "Órdenes obtenidas exitosamente")
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Retorna una orden específica")
    @ApiResponse(responseCode = "200", description = "Orden encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener órdenes de usuario", description = "Retorna todas las órdenes de un usuario")
    @ApiResponse(responseCode = "200", description = "Órdenes obtenidas")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<List<Order>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Obtener detalle de orden", description = "Retorna los detalles completos de una orden")
    @ApiResponse(responseCode = "200", description = "Detalle obtenido")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    public ResponseEntity<OrderResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithUser(id));
    }

    @PostMapping
    @Operation(summary = "Crear orden", description = "Crea una nueva orden")
    @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<Order> create(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de orden", description = "Cambia el estado de una orden")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    public ResponseEntity<Order> updateStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden", description = "Elimina una orden")
    @ApiResponse(responseCode = "204", description = "Orden eliminada")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}