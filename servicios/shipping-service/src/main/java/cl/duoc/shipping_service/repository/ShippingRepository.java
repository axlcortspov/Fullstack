package cl.duoc.shipping_service.repository;

import cl.duoc.shipping_service.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    List<Shipping> findByOrderId(Long orderId);
}