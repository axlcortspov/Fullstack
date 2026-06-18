package cl.duoc.order_service.controller;

import cl.duoc.order_service.dto.OrderRequestDTO;
import cl.duoc.order_service.model.Order;
import cl.duoc.order_service.service.OrderService;
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
class OrderControllerTest {

    @Mock
    private OrderService service;

    @InjectMocks
    private OrderController controller;

    private MockMvc mockMvc;

    @Test
    void shouldReturnAllOrdersWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        List<Order> orders = List.of(
                new Order(1L, 1L, 299.99, "PENDING"),
                new Order(2L, 2L, 599.50, "CONFIRMED")
        );

        when(service.findAll()).thenReturn(orders);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("CONFIRMED"));

        verify(service, times(1)).findAll();
    }

    @Test
    void shouldReturnOrderByIdWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Order order = new Order(1L, 1L, 299.99, "PENDING");

        when(service.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(service, times(1)).findById(1L);
    }

    @Test
    void shouldCreateOrderWithStatus201() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Order order = new Order(1L, 1L, 299.99, "PENDING");

        when(service.create(any(OrderRequestDTO.class))).thenReturn(order);

        mockMvc.perform(post("/api/v1/orders")
                .contentType("application/json")
                .content("{\"userId\":1,\"total\":299.99}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).create(any(OrderRequestDTO.class));
    }

    @Test
    void shouldUpdateOrderStatusWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Order order = new Order(1L, 1L, 299.99, "CONFIRMED");

        when(service.updateStatus(1L, "CONFIRMED")).thenReturn(order);

        mockMvc.perform(put("/api/v1/orders/1/status?status=CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(service, times(1)).updateStatus(1L, "CONFIRMED");
    }

    @Test
    void shouldDeleteOrderWithStatus204() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
    }
}
