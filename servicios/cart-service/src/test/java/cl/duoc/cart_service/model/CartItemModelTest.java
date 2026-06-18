package cl.duoc.cart_service.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartItemModelTest {

    @Test
    void shouldCreateEmptyCartItem() {
        CartItem item = new CartItem();

        assertNotNull(item);
        assertNull(item.getId());
        assertNull(item.getUserId());
        assertNull(item.getProductId());
        assertNull(item.getQuantity());
    }

    @Test
    void shouldCreateCartItemWithAllArgsConstructor() {
        CartItem item = new CartItem(1L, 1L, 5L, 2);

        assertEquals(1L, item.getId());
        assertEquals(1L, item.getUserId());
        assertEquals(5L, item.getProductId());
        assertEquals(2, item.getQuantity());
    }

    @Test
    void shouldSetAndGetValues() {
        CartItem item = new CartItem();

        item.setId(2L);
        item.setUserId(3L);
        item.setProductId(10L);
        item.setQuantity(5);

        assertEquals(2L, item.getId());
        assertEquals(3L, item.getUserId());
        assertEquals(10L, item.getProductId());
        assertEquals(5, item.getQuantity());
    }
}
