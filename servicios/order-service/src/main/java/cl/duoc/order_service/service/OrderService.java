package cl.duoc.order_service.service;

import cl.duoc.order_service.client.UserClient;
import cl.duoc.order_service.dto.OrderRequestDTO;
import cl.duoc.order_service.dto.OrderResponseDTO;
import cl.duoc.order_service.dto.UserDTO;
import cl.duoc.order_service.exception.OrderNotFoundException;
import cl.duoc.order_service.exception.ServiceUnavailableException;
import cl.duoc.order_service.exception.UserNotFoundException;
import cl.duoc.order_service.model.Order;
import cl.duoc.order_service.repository.OrderRepository;
import feign.FeignException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository repository;
    private final UserClient userClient;

    public OrderService(OrderRepository repository, UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Order findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public List<Order> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public Order create(OrderRequestDTO dto) {
        log.info("[order-service] Validando usuario id={} en user-service", dto.getUserId());
        try {
            UserDTO user = userClient.getUserById(dto.getUserId());
            log.info("[order-service] Usuario encontrado: name={}", user.getName());
        } catch (FeignException.NotFound ex) {
            log.warn("[order-service] Usuario id={} no encontrado en user-service", dto.getUserId());
            throw new UserNotFoundException(dto.getUserId());
        } catch (FeignException ex) {
            log.error("[order-service] Error al contactar user-service: {}", ex.getMessage());
            throw new ServiceUnavailableException("user-service");
        }

        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setTotal(dto.getTotal());
        order.setStatus("PENDING");

        Order saved = repository.save(order);
        log.info("[order-service] Order creada con id={}", saved.getId());
        return saved;
    }

    public OrderResponseDTO findByIdWithUser(Long id) {
        Order order = repository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));

        log.info("[order-service] Obteniendo usuario id={} para order id={}", order.getUserId(), id);
        UserDTO user;
        try {
            user = userClient.getUserById(order.getUserId());
            log.info("[order-service] Usuario obtenido para order id={}", id);
        } catch (FeignException.NotFound ex) {
            log.warn("[order-service] Usuario id={} no encontrado al buscar order id={}", order.getUserId(), id);
            throw new UserNotFoundException(order.getUserId());
        } catch (FeignException ex) {
            log.error("[order-service] user-service no disponible al buscar order id={}: {}", id, ex.getMessage());
            throw new ServiceUnavailableException("user-service");
        }

        return new OrderResponseDTO(order.getId(), order.getStatus(), order.getTotal(), user);
    }

    public Order updateStatus(Long id, String status) {
        Order o = repository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
        o.setStatus(status);
        log.info("[order-service] Order id={} actualizada a status={}", id, status);
        return repository.save(o);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("[order-service] Order id={} eliminada", id);
    }
}