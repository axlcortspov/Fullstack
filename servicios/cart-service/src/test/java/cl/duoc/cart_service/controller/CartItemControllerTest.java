package cl.duoc.cart_service.controller;

import cl.duoc.cart_service.model.CartItem;
import cl.duoc.cart_service.service.CartItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartItemControllerTest {

    @Mock
    private CartItemService service;

    @InjectMocks
    private CartItemController controller;

    private MockMvc mockMvc;

    @Test
    void shouldReturnAllCartItemsWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        List<CartItem> items = List.of(
                new CartItem(1L, 1L, 5L, 2),
                new CartItem(2L, 1L, 10L, 1)
        );

        when(service.findAll()).thenReturn(items);

        mockMvc.perform(get("/api/v1/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[1].quantity").value(1));

        verify(service, times(1)).findAll();
    }

    @Test
    void shouldReturnCartItemsByUserIdWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        List<CartItem> items = List.of(
                new CartItem(1L, 1L, 5L, 2),
                new CartItem(2L, 1L, 10L, 1)
        );

        when(service.findByUserId(1L)).thenReturn(items);

        mockMvc.perform(get("/api/v1/cart/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(2));

        verify(service, times(1)).findByUserId(1L);
    }

    @Test
    void shouldCreateCartItemWithStatus201() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        CartItem item = new CartItem(1L, 1L, 5L, 2);

        when(service.save(any(CartItem.class))).thenReturn(item);

        mockMvc.perform(post("/api/v1/cart")
                .contentType("application/json")
                .content("{\"userId\":1,\"productId\":5,\"quantity\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service, times(1)).save(any(CartItem.class));
    }

    @Test
    void shouldUpdateCartItemWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        CartItem item = new CartItem(1L, 1L, 5L, 5);

        when(service.update(anyLong(), any(CartItem.class))).thenReturn(item);

        mockMvc.perform(put("/api/v1/cart/1")
                .contentType("application/json")
                .content("{\"userId\":1,\"productId\":5,\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));

        verify(service, times(1)).update(anyLong(), any(CartItem.class));
    }

    @Test
    void shouldDeleteCartItemWithStatus204() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete("/api/v1/cart/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
    }
}
