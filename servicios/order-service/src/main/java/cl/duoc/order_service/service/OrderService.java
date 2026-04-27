package cl.duoc.order_service.service;

import cl.duoc.order_service.model.Order;
import cl.duoc.order_service.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public List<Order> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public Order create(Order order) {
        order.setStatus("PENDING");
        return repository.save(order);
    }

    public Order updateStatus(Long id, String status) {
        Order o = repository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Order no encontrada"));

        o.setStatus(status);
        return repository.save(o);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
