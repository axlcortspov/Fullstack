package cl.duoc.cart_service.service;

import cl.duoc.cart_service.model.CartItem;
import cl.duoc.cart_service.repository.CartItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final CartItemRepository repository;

    public CartItemService(CartItemRepository repository) {
        this.repository = repository;
    }

    public List<CartItem> findAll() {
        return repository.findAll();
    }

    public List<CartItem> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public CartItem save(CartItem item) {
        return repository.save(item);
    }

    public CartItem update(Long id, CartItem item) {
        CartItem existing = repository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        existing.setUserId(item.getUserId());
        existing.setProductId(item.getProductId());
        existing.setQuantity(item.getQuantity());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
