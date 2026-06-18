package cl.duoc.payment_service.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentModelTest {

    @Test
    void shouldCreateEmptyPayment() {
        Payment payment = new Payment();

        assertNotNull(payment);
        assertNull(payment.getId());
        assertNull(payment.getOrderId());
        assertNull(payment.getAmount());
        assertNull(payment.getPaymentMethod());
        assertNull(payment.getStatus());
    }

    @Test
    void shouldCreatePaymentWithAllArgsConstructor() {
        Payment payment = new Payment(1L, 10L, 299.99, "CREDIT_CARD", "COMPLETED");

        assertEquals(1L, payment.getId());
        assertEquals(10L, payment.getOrderId());
        assertEquals(299.99, payment.getAmount());
        assertEquals("CREDIT_CARD", payment.getPaymentMethod());
        assertEquals("COMPLETED", payment.getStatus());
    }

    @Test
    void shouldSetAndGetValues() {
        Payment payment = new Payment();

        payment.setId(2L);
        payment.setOrderId(15L);
        payment.setAmount(599.50);
        payment.setPaymentMethod("DEBIT_CARD");
        payment.setStatus("PENDING");

        assertEquals(2L, payment.getId());
        assertEquals(15L, payment.getOrderId());
        assertEquals(599.50, payment.getAmount());
        assertEquals("DEBIT_CARD", payment.getPaymentMethod());
        assertEquals("PENDING", payment.getStatus());
    }
}
