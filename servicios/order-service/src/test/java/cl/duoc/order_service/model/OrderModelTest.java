package cl.duoc.order_service.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderModelTest {

    @Test
    void shouldCreateEmptyOrder() {
        Order order = new Order();

        assertNotNull(order);
        assertNull(order.getId());
        assertNull(order.getUserId());
        assertNull(order.getTotal());
        assertNull(order.getStatus());
    }

    @Test
    void shouldCreateOrderWithAllArgsConstructor() {
        Order order = new Order(1L, 1L, 299.99, "PENDING");

        assertEquals(1L, order.getId());
        assertEquals(1L, order.getUserId());
        assertEquals(299.99, order.getTotal());
        assertEquals("PENDING", order.getStatus());
    }

    @Test
    void shouldSetAndGetValues() {
        Order order = new Order();

        order.setId(2L);
        order.setUserId(5L);
        order.setTotal(599.50);
        order.setStatus("CONFIRMED");

        assertEquals(2L, order.getId());
        assertEquals(5L, order.getUserId());
        assertEquals(599.50, order.getTotal());
        assertEquals("CONFIRMED", order.getStatus());
    }
}
