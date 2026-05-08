package cl.duoc.payment_service.service;

import cl.duoc.payment_service.model.Payment;
import cl.duoc.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public List<Payment> findAll() {
        return repository.findAll();
    }

    public List<Payment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public Payment create(Payment payment) {
        if (payment.getStatus() == null || payment.getStatus().isEmpty()) {
            payment.setStatus("PENDING");
        }

        return repository.save(payment);
    }

    public Payment updateStatus(Long id, String status) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        payment.setStatus(status);
        return repository.save(payment);
    }

    public Payment update(Long id, Payment paymentData) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        payment.setOrderId(paymentData.getOrderId());
        payment.setAmount(paymentData.getAmount());
        payment.setPaymentMethod(paymentData.getPaymentMethod());
        payment.setStatus(paymentData.getStatus());

        return repository.save(payment);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}