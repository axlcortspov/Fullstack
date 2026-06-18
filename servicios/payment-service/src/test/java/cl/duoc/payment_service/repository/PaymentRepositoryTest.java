package cl.duoc.payment_service.repository;

import cl.duoc.payment_service.model.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository repo;

    @Test
    void shouldSavePayment() {
        Payment payment = new Payment(null, 10L, 299.99, "CREDIT_CARD", "PENDING");

        Payment saved = repo.save(payment);

        assertNotNull(saved.getId());
        assertEquals(10L, saved.getOrderId());
        assertEquals("PENDING", saved.getStatus());
    }

    @Test
    void shouldFindPaymentById() {
        Payment payment = new Payment(null, 10L, 299.99, "CREDIT_CARD", "PENDING");
        Payment saved = repo.save(payment);

        Optional<Payment> found = repo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(10L, found.get().getOrderId());
    }

    @Test
    void shouldReturnAllPayments() {
        repo.save(new Payment(null, 10L, 100.0, "CREDIT_CARD", "PENDING"));
        repo.save(new Payment(null, 11L, 200.0, "DEBIT_CARD", "COMPLETED"));
        repo.save(new Payment(null, 12L, 300.0, "CREDIT_CARD", "COMPLETED"));

        assertEquals(3, repo.findAll().size());
    }

    @Test
    void shouldFindPaymentsByOrderId() {
        repo.save(new Payment(null, 10L, 100.0, "CREDIT_CARD", "PENDING"));
        repo.save(new Payment(null, 10L, 200.0, "DEBIT_CARD", "COMPLETED"));
        repo.save(new Payment(null, 11L, 300.0, "CREDIT_CARD", "COMPLETED"));

        List<Payment> payments = repo.findByOrderId(10L);

        assertEquals(2, payments.size());
    }
}
