package cl.duoc.cart_service.controller;

import cl.duoc.cart_service.model.CartItem;
import cl.duoc.cart_service.service.CartItemService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartItemController {

    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<CartItem> getAll() {
        return service.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<CartItem> getByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @PostMapping
    public CartItem create(@RequestBody CartItem item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public CartItem update(@PathVariable Long id, @RequestBody CartItem item) {
        return service.update(id, item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
