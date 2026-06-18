package cl.duoc.order_service.repository;

import cl.duoc.order_service.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repo;

    @Test
    void shouldSaveOrder() {
        Order order = new Order(null, 1L, 299.99, "PENDING");

        Order saved = repo.save(order);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getUserId());
        assertEquals("PENDING", saved.getStatus());
    }

    @Test
    void shouldFindOrderById() {
        Order order = new Order(null, 1L, 299.99, "PENDING");
        Order saved = repo.save(order);

        Optional<Order> found = repo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getUserId());
    }

    @Test
    void shouldReturnAllOrders() {
        repo.save(new Order(null, 1L, 100.0, "PENDING"));
        repo.save(new Order(null, 2L, 200.0, "CONFIRMED"));
        repo.save(new Order(null, 3L, 300.0, "SHIPPED"));

        assertEquals(3, repo.findAll().size());
    }

    @Test
    void shouldFindOrdersByUserId() {
        repo.save(new Order(null, 1L, 100.0, "PENDING"));
        repo.save(new Order(null, 1L, 200.0, "CONFIRMED"));
        repo.save(new Order(null, 2L, 300.0, "SHIPPED"));

        List<Order> orders = repo.findByUserId(1L);

        assertEquals(2, orders.size());
    }
}
