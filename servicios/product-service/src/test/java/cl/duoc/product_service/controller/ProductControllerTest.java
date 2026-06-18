package cl.duoc.product_service.controller;

import cl.duoc.product_service.model.Product;
import cl.duoc.product_service.service.ProductService;
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
class ProductControllerTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    private MockMvc mockMvc;

    @Test
    void shouldReturnAllProductsWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        List<Product> products = List.of(
                new Product(1L, "Laptop Dell", 999.99),
                new Product(2L, "Monitor LG", 299.50)
        );

        when(service.getAll()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop Dell"))
                .andExpect(jsonPath("$[0].price").value(999.99));

        verify(service, times(1)).getAll();
    }

    @Test
    void shouldCreateProductWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Product product = new Product(1L, "Teclado", 149.99);

        when(service.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                .contentType("application/json")
                .content("{\"name\":\"Teclado\",\"price\":149.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Teclado"));

        verify(service, times(1)).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        doNothing().when(service).delete(anyLong());

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(1L);
    }

    @Test
    void shouldReturnProductByIdWithStatus200() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        Product product = new Product(1L, "Laptop Dell", 999.99);

        when(service.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop Dell"))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(service, times(1)).findById(1L);
    }
}
