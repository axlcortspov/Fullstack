package cl.duoc.product_service.service;

import cl.duoc.product_service.model.Product;
import cl.duoc.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldReturnAllProducts() {
        List<Product> products = List.of(
                new Product(1L, "Laptop Dell", 999.99),
                new Product(2L, "Monitor LG 24", 299.50)
        );

        when(repo.findAll()).thenReturn(products);

        List<Product> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Laptop Dell", result.get(0).getName());

        verify(repo, times(1)).findAll();
    }

    @Test
    void shouldSaveProduct() {
        Product product = new Product(1L, "Teclado Mecánico", 149.99);

        when(repo.save(any(Product.class))).thenReturn(product);

        Product result = service.save(product);

        assertNotNull(result);
        assertEquals("Teclado Mecánico", result.getName());
        assertEquals(149.99, result.getPrice());

        verify(repo, times(1)).save(product);
    }

    @Test
    void shouldFindProductById() {
        Product product = new Product(1L, "Laptop Dell", 999.99);

        when(repo.findById(1L)).thenReturn(Optional.of(product));

        Product result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Laptop Dell", result.getName());

        verify(repo, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.findById(99L)
        );

        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    void shouldDeleteProduct() {
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void shouldValidatePriceIsPositive() {
        Product product = new Product(1L, "Product", -100.0);

        when(repo.save(any(Product.class))).thenReturn(product);

        Product result = service.save(product);

        assertNotNull(result);
        assertTrue(result.getPrice() < 0);
    }
}
