package cl.duoc.product_service.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelTest {

    @Test
    void shouldCreateEmptyProduct() {
        Product product = new Product();

        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getName());
        assertEquals(0, product.getPrice());
    }

    @Test
    void shouldCreateProductWithAllArgsConstructor() {
        Product product = new Product(1L, "Laptop Dell", 999.99);

        assertEquals(1L, product.getId());
        assertEquals("Laptop Dell", product.getName());
        assertEquals(999.99, product.getPrice());
    }

    @Test
    void shouldSetAndGetValues() {
        Product product = new Product();

        product.setId(2L);
        product.setName("Monitor LG 24");
        product.setPrice(299.50);

        assertEquals(2L, product.getId());
        assertEquals("Monitor LG 24", product.getName());
        assertEquals(299.50, product.getPrice());
    }
}
