package cl.duoc.payment_service.service;

import cl.duoc.payment_service.client.OrderClient;
import cl.duoc.payment_service.dto.OrderDTO;
import cl.duoc.payment_service.dto.PaymentResponseDTO;
import cl.duoc.payment_service.exception.OrderNotFoundException;
import cl.duoc.payment_service.exception.PaymentNotFoundException;
import cl.duoc.payment_service.exception.ServiceUnavailableException;
import cl.duoc.payment_service.model.Payment;
import cl.duoc.payment_service.repository.PaymentRepository;
import feign.FeignException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final OrderClient orderClient;

    public PaymentService(PaymentRepository repository, OrderClient orderClient) {
        this.repository = repository;
        this.orderClient = orderClient;
    }

    public List<Payment> findAll() {
        return repository.findAll();
    }

    public List<Payment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    // Al crear un pago, valida que la orden exista en order-service
    public Payment create(Payment payment) {
        log.info("[payment-service] Validando orden id={} en order-service", payment.getOrderId());
        try {
            OrderDTO order = orderClient.getOrderById(payment.getOrderId());
            log.info("[payment-service] Orden encontrada: status={}, total={}", order.getStatus(), order.getTotal());
        } catch (FeignException.NotFound ex) {
            log.warn("[payment-service] Orden id={} no encontrada en order-service", payment.getOrderId());
            throw new OrderNotFoundException(payment.getOrderId());
        } catch (FeignException ex) {
            log.error("[payment-service] order-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("order-service");
        }

        if (payment.getStatus() == null || payment.getStatus().isEmpty()) {
            payment.setStatus("PENDING");
        }

        Payment saved = repository.save(payment);
        log.info("[payment-service] Pago creado con id={}", saved.getId());
        return saved;
    }

    // Devuelve el pago enriquecido con los datos de la orden
    public PaymentResponseDTO findByIdWithOrder(Long id) {
        Payment payment = repository.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException(id));

        log.info("[payment-service] Obteniendo orden id={} para pago id={}", payment.getOrderId(), id);
        OrderDTO order;
        try {
            order = orderClient.getOrderById(payment.getOrderId());
            log.info("[payment-service] Orden obtenida para pago id={}", id);
        } catch (FeignException.NotFound ex) {
            log.warn("[payment-service] Orden id={} no encontrada al buscar pago id={}", payment.getOrderId(), id);
            throw new OrderNotFoundException(payment.getOrderId());
        } catch (FeignException ex) {
            log.error("[payment-service] order-service no disponible al buscar pago id={}", id);
            throw new ServiceUnavailableException("order-service");
        }

        return new PaymentResponseDTO(payment.getId(), payment.getAmount(), payment.getPaymentMethod(), payment.getStatus(), order);
    }

    public Payment updateStatus(Long id, String status) {
        Payment payment = repository.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException(id));
        payment.setStatus(status);
        log.info("[payment-service] Pago id={} actualizado a status={}", id, status);
        return repository.save(payment);
    }

    public Payment update(Long id, Payment paymentData) {
        Payment payment = repository.findById(id)
            .orElseThrow(() -> new PaymentNotFoundException(id));
        payment.setOrderId(paymentData.getOrderId());
        payment.setAmount(paymentData.getAmount());
        payment.setPaymentMethod(paymentData.getPaymentMethod());
        payment.setStatus(paymentData.getStatus());
        log.info("[payment-service] Pago id={} actualizado", id);
        return repository.save(payment);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new PaymentNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("[payment-service] Pago id={} eliminado", id);
    }
}