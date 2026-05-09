package cl.duoc.shipping_service.controller;

import cl.duoc.shipping_service.model.Shipping;
import cl.duoc.shipping_service.service.ShippingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final ShippingService service;

    public ShippingController(ShippingService service) {
        this.service = service;
    }

    @GetMapping
    public List<Shipping> getAll() {
        return service.findAll();
    }

    @GetMapping("/order/{orderId}")
    public List<Shipping> getByOrderId(@PathVariable Long orderId) {
        return service.findByOrderId(orderId);
    }

    @PostMapping
    public Shipping create(@RequestBody Shipping shipping) {
        return service.create(shipping);
    }

    @PutMapping("/{id}")
    public Shipping update(@PathVariable Long id, @RequestBody Shipping shipping) {
        return service.update(id, shipping);
    }

    @PutMapping("/{id}/status")
    public Shipping updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}