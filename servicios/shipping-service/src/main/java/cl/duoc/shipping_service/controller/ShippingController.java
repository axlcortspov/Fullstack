package cl.duoc.shipping_service.controller;

import cl.duoc.shipping_service.dto.ShippingRequestDTO;
import cl.duoc.shipping_service.dto.ShippingResponseDTO;
import cl.duoc.shipping_service.model.Shipping;
import cl.duoc.shipping_service.service.ShippingService;
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
@RequestMapping("/api/v1/shipping")
@Tag(name = "Shipping", description = "API de envíos de órdenes")
public class ShippingController {

    private final ShippingService service;

    public ShippingController(ShippingService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los envíos", description = "Retorna todos los registros de envío")
    @ApiResponse(responseCode = "200", description = "Envíos obtenidos exitosamente")
    public ResponseEntity<List<Shipping>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Obtener envíos de orden", description = "Retorna todos los envíos de una orden específica")
    @ApiResponse(responseCode = "200", description = "Envíos obtenidos")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    public ResponseEntity<List<Shipping>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.findByOrderId(orderId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Obtener detalle de envío", description = "Retorna los detalles completos de un envío")
    @ApiResponse(responseCode = "200", description = "Detalle obtenido")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    public ResponseEntity<ShippingResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithOrder(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar envío", description = "Actualiza los datos de un envío")
    @ApiResponse(responseCode = "200", description = "Envío actualizado")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    public ResponseEntity<Shipping> update(@PathVariable Long id, @RequestBody Shipping shipping) {
        return ResponseEntity.ok(service.update(id, shipping));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de envío", description = "Cambia el estado de un envío")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    public ResponseEntity<Shipping> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar envío", description = "Elimina un registro de envío")
    @ApiResponse(responseCode = "204", description = "Envío eliminado")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "Crear envío", description = "Registra un nuevo envío para una orden")
    @ApiResponse(responseCode = "201", description = "Envío creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Shipping.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<Shipping> create(@Valid @RequestBody ShippingRequestDTO dto) {
        Shipping shipping = new Shipping();
        shipping.setOrderId(dto.getOrderId());
        shipping.setAddress(dto.getAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(shipping));
    }
}