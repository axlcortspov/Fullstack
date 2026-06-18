package cl.duoc.payment_service.controller;

import cl.duoc.payment_service.model.Payment;
import cl.duoc.payment_service.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService service;

    @InjectMocks
    private PaymentController controller;

    private MockMvc mockMvc;

    @Test
    void shouldReturnAllPaymentsWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        List<Payment> payments = List.of(
                new Payment(1L, 10L, 299.99, "CREDIT_CARD", "COMPLETED"),
                new Payment(2L, 11L, 599.50, "DEBIT_CARD", "PENDING")
        );

        when(service.findAll()).thenReturn(payments);

        mockMvc.perform(get("/api/v1/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[1].status").value("PENDING"));

        verify(service, times(1)).findAll();
    }

    @Test
    void shouldReturnPaymentsByOrderIdWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        List<Payment> payments = List.of(
                new Payment(1L, 10L, 299.99, "CREDIT_CARD", "COMPLETED")
        );

        when(service.findByOrderId(10L)).thenReturn(payments);

        mockMvc.perform(get("/api/v1/payments/order/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(10L));

        verify(service, times(1)).findByOrderId(10L);
    }

    @Test
    void shouldCreatePaymentWithStatus201() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Payment payment = new Payment(1L, 10L, 299.99, "CREDIT_CARD", "PENDING");

        when(service.create(any(Payment.class))).thenReturn(payment);

        mockMvc.perform(post("/api/v1/payments")
                .contentType("application/json")
                .content("{\"orderId\":10,\"amount\":299.99,\"paymentMethod\":\"CREDIT_CARD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).create(any(Payment.class));
    }

    @Test
    void shouldUpdatePaymentWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Payment payment = new Payment(1L, 10L, 299.99, "CREDIT_CARD", "COMPLETED");

        when(service.update(anyLong(), any(Payment.class))).thenReturn(payment);

        mockMvc.perform(put("/api/v1/payments/1")
                .contentType("application/json")
                .content("{\"orderId\":10,\"amount\":299.99,\"paymentMethod\":\"CREDIT_CARD\",\"status\":\"COMPLETED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(service, times(1)).update(anyLong(), any(Payment.class));
    }

    @Test
    void shouldUpdatePaymentStatusWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Payment payment = new Payment(1L, 10L, 299.99, "CREDIT_CARD", "COMPLETED");

        when(service.updateStatus(1L, "COMPLETED")).thenReturn(payment);

        mockMvc.perform(put("/api/v1/payments/1/status?status=COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(service, times(1)).updateStatus(1L, "COMPLETED");
    }

    @Test
    void shouldDeletePaymentWithStatus204() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete("/api/v1/payments/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
    }
}
