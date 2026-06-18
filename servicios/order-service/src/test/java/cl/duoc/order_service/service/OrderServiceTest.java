package cl.duoc.order_service.service;

import cl.duoc.order_service.client.UserClient;
import cl.duoc.order_service.dto.OrderRequestDTO;
import cl.duoc.order_service.dto.UserDTO;
import cl.duoc.order_service.exception.OrderNotFoundException;
import cl.duoc.order_service.model.Order;
import cl.duoc.order_service.repository.OrderRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private OrderService service;

    @Test
    void shouldReturnAllOrders() {
        List<Order> orders = List.of(
                new Order(1L, 1L, 299.99, "PENDING"),
                new Order(2L, 2L, 599.50, "CONFIRMED")
        );

        when(repository.findAll()).thenReturn(orders);

        List<Order> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("PENDING", result.get(0).getStatus());

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldFindOrderById() {
        Order order = new Order(1L, 1L, 299.99, "PENDING");

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        Order result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.findById(99L));

        verify(repository, times(1)).findById(99L);
    }

    @Test
    void shouldCreateOrderWithValidUser() {
        OrderRequestDTO dto = new OrderRequestDTO(1L, 299.99);
        UserDTO userDTO = new UserDTO(1L, "Juan Pérez", "juan@gmail.com");
        Order order = new Order(1L, 1L, 299.99, "PENDING");

        when(userClient.getUserById(1L)).thenReturn(userDTO);
        when(repository.save(any(Order.class))).thenReturn(order);

        Order result = service.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());

        verify(userClient, times(1)).getUserById(1L);
        verify(repository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldUpdateOrderStatus() {
        Order order = new Order(1L, 1L, 299.99, "PENDING");

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.save(any(Order.class))).thenReturn(order);

        Order result = service.updateStatus(1L, "CONFIRMED");

        assertNotNull(result);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldDeleteOrder() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
