package cl.duoc.payment_service.service;

import cl.duoc.payment_service.client.OrderClient;
import cl.duoc.payment_service.dto.OrderDTO;
import cl.duoc.payment_service.exception.OrderNotFoundException;
import cl.duoc.payment_service.exception.PaymentNotFoundException;
import cl.duoc.payment_service.model.Payment;
import cl.duoc.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private PaymentService service;

    @Test
    void shouldReturnAllPayments() {
        List<Payment> payments = List.of(
                new Payment(1L, 10L, 299.99, "CREDIT_CARD", "COMPLETED"),
                new Payment(2L, 11L, 599.50, "DEBIT_CARD", "PENDING")
        );

        when(repository.findAll()).thenReturn(payments);

        List<Payment> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldFindPaymentsByOrderId() {
        List<Payment> payments = List.of(
                new Payment(1L, 10L, 299.99, "CREDIT_CARD", "COMPLETED")
        );

        when(repository.findByOrderId(10L)).thenReturn(payments);

        List<Payment> result = service.findByOrderId(10L);

        assertEquals(1, result.size());
        verify(repository, times(1)).findByOrderId(10L);
    }

    @Test
    void shouldCreatePaymentWithValidOrder() {
        Payment payment = new Payment(null, 10L, 299.99, "CREDIT_CARD", null);
        OrderDTO orderDTO = new OrderDTO(10L, 1L, 299.99, "PENDING");
        Payment savedPayment = new Payment(1L, 10L, 299.99, "CREDIT_CARD", "PENDING");

        when(orderClient.getOrderById(10L)).thenReturn(orderDTO);
        when(repository.save(any(Payment.class))).thenReturn(savedPayment);

        Payment result = service.create(payment);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        verify(orderClient, times(1)).getOrderById(10L);
        verify(repository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldUpdatePaymentStatus() {
        Payment payment = new Payment(1L, 10L, 299.99, "CREDIT_CARD", "PENDING");

        when(repository.findById(1L)).thenReturn(Optional.of(payment));
        when(repository.save(any(Payment.class))).thenReturn(payment);

        Payment result = service.updateStatus(1L, "COMPLETED");

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldUpdatePayment() {
        Payment existing = new Payment(1L, 10L, 299.99, "CREDIT_CARD", "PENDING");
        Payment updated = new Payment(1L, 10L, 350.00, "DEBIT_CARD", "COMPLETED");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Payment.class))).thenReturn(updated);

        Payment result = service.update(1L, updated);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Payment.class));
    }

    @Test
    void shouldDeletePayment() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowPaymentNotFoundExceptionWhenDeletingNonExistent() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(PaymentNotFoundException.class, () -> service.delete(99L));

        verify(repository, times(1)).existsById(99L);
    }
}
