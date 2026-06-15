package cl.duoc.payment_service.controller;

import cl.duoc.payment_service.dto.PaymentRequestDTO;
import cl.duoc.payment_service.dto.PaymentResponseDTO;
import cl.duoc.payment_service.model.Payment;
import cl.duoc.payment_service.service.PaymentService;
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
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "API de pagos de órdenes")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pagos", description = "Retorna todos los pagos registrados")
    @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente")
    public ResponseEntity<List<Payment>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Obtener pagos de orden", description = "Retorna todos los pagos de una orden específica")
    @ApiResponse(responseCode = "200", description = "Pagos obtenidos")
    @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    public ResponseEntity<List<Payment>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.findByOrderId(orderId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Obtener detalle de pago", description = "Retorna los detalles completos de un pago")
    @ApiResponse(responseCode = "200", description = "Detalle obtenido")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<PaymentResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithOrder(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago", description = "Actualiza los datos de un pago")
    @ApiResponse(responseCode = "200", description = "Pago actualizado")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment payment) {
        return ResponseEntity.ok(service.update(id, payment));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de pago", description = "Cambia el estado de un pago")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<Payment> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina un registro de pago")
    @ApiResponse(responseCode = "204", description = "Pago eliminado")
    @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "Crear pago", description = "Registra un nuevo pago")
    @ApiResponse(responseCode = "201", description = "Pago creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<Payment> create(@Valid @RequestBody PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(payment));
    }
}