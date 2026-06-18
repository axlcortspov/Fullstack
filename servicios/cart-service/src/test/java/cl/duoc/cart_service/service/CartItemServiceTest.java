package cl.duoc.cart_service.service;

import cl.duoc.cart_service.client.ProductClient;
import cl.duoc.cart_service.client.UserClient;
import cl.duoc.cart_service.dto.ProductDTO;
import cl.duoc.cart_service.dto.UserDTO;
import cl.duoc.cart_service.exception.CartItemNotFoundException;
import cl.duoc.cart_service.model.CartItem;
import cl.duoc.cart_service.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private CartItemService service;

    @Test
    void shouldReturnAllCartItems() {
        List<CartItem> items = List.of(
                new CartItem(1L, 1L, 5L, 2),
                new CartItem(2L, 1L, 10L, 1)
        );

        when(repository.findAll()).thenReturn(items);

        List<CartItem> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldFindCartItemsByUserId() {
        List<CartItem> items = List.of(
                new CartItem(1L, 1L, 5L, 2),
                new CartItem(2L, 1L, 10L, 1)
        );

        when(repository.findByUserId(1L)).thenReturn(items);

        List<CartItem> result = service.findByUserId(1L);

        assertEquals(2, result.size());
        verify(repository, times(1)).findByUserId(1L);
    }

    @Test
    void shouldSaveCartItem() {
        CartItem item = new CartItem(1L, 1L, 5L, 2);
        UserDTO userDTO = new UserDTO(1L, "Juan", "juan@gmail.com");
        ProductDTO productDTO = new ProductDTO(5L, "Laptop", 999.99);

        when(userClient.getUserById(1L)).thenReturn(userDTO);
        when(productClient.getProductById(5L)).thenReturn(productDTO);
        when(repository.save(any(CartItem.class))).thenReturn(item);

        CartItem result = service.save(item);

        assertNotNull(result);
        assertEquals(2, result.getQuantity());

        verify(userClient, times(1)).getUserById(1L);
        verify(productClient, times(1)).getProductById(5L);
        verify(repository, times(1)).save(any(CartItem.class));
    }

    @Test
    void shouldUpdateCartItem() {
        CartItem existing = new CartItem(1L, 1L, 5L, 2);
        CartItem updated = new CartItem(1L, 1L, 5L, 5);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(CartItem.class))).thenReturn(updated);

        CartItem result = service.update(1L, updated);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(CartItem.class));
    }

    @Test
    void shouldDeleteCartItem() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowCartItemNotFoundExceptionWhenDeletingNonExistent() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(CartItemNotFoundException.class, () -> service.delete(99L));

        verify(repository, times(1)).existsById(99L);
    }
}
