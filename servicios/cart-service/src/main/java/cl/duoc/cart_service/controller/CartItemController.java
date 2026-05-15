package cl.duoc.cart_service.controller;

import cl.duoc.cart_service.dto.CartItemRequestDTO;
import cl.duoc.cart_service.dto.CartItemResponseDTO;
import cl.duoc.cart_service.model.CartItem;
import cl.duoc.cart_service.service.CartItemService;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartItemController {

    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItem>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<CartItemResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithDetails(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> update(@PathVariable Long id, @RequestBody CartItem item) {
        return ResponseEntity.ok(service.update(id, item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<CartItem> create(@Valid @RequestBody CartItemRequestDTO dto) {
        CartItem item = new CartItem();
        item.setUserId(dto.getUserId());
        item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(item));
    }
}