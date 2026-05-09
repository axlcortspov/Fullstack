package cl.duoc.shipping_service.service;

import cl.duoc.shipping_service.model.Shipping;
import cl.duoc.shipping_service.repository.ShippingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService {

    private final ShippingRepository repository;

    public ShippingService(ShippingRepository repository) {
        this.repository = repository;
    }

    public List<Shipping> findAll() {
        return repository.findAll();
    }

    public List<Shipping> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public Shipping create(Shipping shipping) {
        if (shipping.getStatus() == null || shipping.getStatus().isEmpty()) {
            shipping.setStatus("PENDING");
        }

        if (shipping.getTrackingNumber() == null || shipping.getTrackingNumber().isEmpty()) {
            shipping.setTrackingNumber("TRK-" + System.currentTimeMillis());
        }

        return repository.save(shipping);
    }

    public Shipping update(Long id, Shipping shippingData) {
        Shipping shipping = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        shipping.setOrderId(shippingData.getOrderId());
        shipping.setAddress(shippingData.getAddress());
        shipping.setStatus(shippingData.getStatus());
        shipping.setTrackingNumber(shippingData.getTrackingNumber());

        return repository.save(shipping);
    }

    public Shipping updateStatus(Long id, String status) {
        Shipping shipping = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        shipping.setStatus(status);
        return repository.save(shipping);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}