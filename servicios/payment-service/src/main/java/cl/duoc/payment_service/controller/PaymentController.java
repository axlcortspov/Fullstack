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

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.findByOrderId(orderId));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<PaymentResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithOrder(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment payment) {
        return ResponseEntity.ok(service.update(id, payment));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(payment));
    }
}