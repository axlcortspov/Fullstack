package cl.duoc.cart_service.repository;

import cl.duoc.cart_service.model.CartItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository repo;

    @Test
    void shouldSaveCartItem() {
        CartItem item = new CartItem(null, 1L, 5L, 2);

        CartItem saved = repo.save(item);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getUserId());
        assertEquals(5L, saved.getProductId());
        assertEquals(2, saved.getQuantity());
    }

    @Test
    void shouldFindCartItemById() {
        CartItem item = new CartItem(null, 1L, 5L, 2);
        CartItem saved = repo.save(item);

        Optional<CartItem> found = repo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getUserId());
    }

    @Test
    void shouldReturnAllCartItems() {
        repo.save(new CartItem(null, 1L, 5L, 2));
        repo.save(new CartItem(null, 1L, 10L, 1));
        repo.save(new CartItem(null, 2L, 7L, 3));

        assertEquals(3, repo.findAll().size());
    }

    @Test
    void shouldFindCartItemsByUserId() {
        repo.save(new CartItem(null, 1L, 5L, 2));
        repo.save(new CartItem(null, 1L, 10L, 1));
        repo.save(new CartItem(null, 2L, 7L, 3));

        List<CartItem> items = repo.findByUserId(1L);

        assertEquals(2, items.size());
    }
}
