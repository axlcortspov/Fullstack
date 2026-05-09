package cl.duoc.shipping_service.service;

import cl.duoc.shipping_service.client.OrderClient;
import cl.duoc.shipping_service.dto.OrderDTO;
import cl.duoc.shipping_service.dto.ShippingResponseDTO;
import cl.duoc.shipping_service.exception.OrderNotFoundException;
import cl.duoc.shipping_service.exception.ServiceUnavailableException;
import cl.duoc.shipping_service.exception.ShippingNotFoundException;
import cl.duoc.shipping_service.model.Shipping;
import cl.duoc.shipping_service.repository.ShippingRepository;
import feign.FeignException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShippingService {

    private final ShippingRepository repository;
    private final OrderClient orderClient;

    public ShippingService(ShippingRepository repository, OrderClient orderClient) {
        this.repository = repository;
        this.orderClient = orderClient;
    }

    public List<Shipping> findAll() {
        return repository.findAll();
    }

    public List<Shipping> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public Shipping create(Shipping shipping) {
        log.info("[shipping-service] Validando orden id={} en order-service", shipping.getOrderId());
        try {
            OrderDTO order = orderClient.getOrderById(shipping.getOrderId());
            log.info("[shipping-service] Orden encontrada: status={}", order.getStatus());
        } catch (FeignException.NotFound ex) {
            log.warn("[shipping-service] Orden id={} no encontrada en order-service", shipping.getOrderId());
            throw new OrderNotFoundException(shipping.getOrderId());
        } catch (FeignException ex) {
            log.error("[shipping-service] order-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("order-service");
        }

        if (shipping.getStatus() == null || shipping.getStatus().isEmpty()) {
            shipping.setStatus("PENDING");
        }
        if (shipping.getTrackingNumber() == null || shipping.getTrackingNumber().isEmpty()) {
            shipping.setTrackingNumber("TRK-" + System.currentTimeMillis());
        }

        Shipping saved = repository.save(shipping);
        log.info("[shipping-service] Envío creado con id={} tracking={}", saved.getId(), saved.getTrackingNumber());
        return saved;
    }

    public ShippingResponseDTO findByIdWithOrder(Long id) {
        Shipping shipping = repository.findById(id)
            .orElseThrow(() -> new ShippingNotFoundException(id));

        log.info("[shipping-service] Obteniendo orden id={} para envío id={}", shipping.getOrderId(), id);
        OrderDTO order;
        try {
            order = orderClient.getOrderById(shipping.getOrderId());
            log.info("[shipping-service] Orden obtenida para envío id={}", id);
        } catch (FeignException.NotFound ex) {
            log.warn("[shipping-service] Orden id={} no encontrada al buscar envío id={}", shipping.getOrderId(), id);
            throw new OrderNotFoundException(shipping.getOrderId());
        } catch (FeignException ex) {
            log.error("[shipping-service] order-service no disponible al buscar envío id={}", id);
            throw new ServiceUnavailableException("order-service");
        }

        return new ShippingResponseDTO(shipping.getId(), shipping.getAddress(), shipping.getStatus(), shipping.getTrackingNumber(), order);
    }

    public Shipping update(Long id, Shipping shippingData) {
        Shipping shipping = repository.findById(id)
            .orElseThrow(() -> new ShippingNotFoundException(id));
        shipping.setOrderId(shippingData.getOrderId());
        shipping.setAddress(shippingData.getAddress());
        shipping.setStatus(shippingData.getStatus());
        shipping.setTrackingNumber(shippingData.getTrackingNumber());
        log.info("[shipping-service] Envío id={} actualizado", id);
        return repository.save(shipping);
    }

    public Shipping updateStatus(Long id, String status) {
        Shipping shipping = repository.findById(id)
            .orElseThrow(() -> new ShippingNotFoundException(id));
        shipping.setStatus(status);
        log.info("[shipping-service] Envío id={} actualizado a status={}", id, status);
        return repository.save(shipping);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ShippingNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("[shipping-service] Envío id={} eliminado", id);
    }
}