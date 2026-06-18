package cl.duoc.product_service.repository;

import cl.duoc.product_service.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repo;

    @Test
    void shouldSaveProduct() {
        Product product = new Product(null, "Laptop Asus", 1299.99);

        Product saved = repo.save(product);

        assertNotNull(saved.getId());
        assertEquals("Laptop Asus", saved.getName());
        assertEquals(1299.99, saved.getPrice());
    }

    @Test
    void shouldFindProductById() {
        Product product = new Product(null, "Mouse Logitech", 49.99);
        Product saved = repo.save(product);

        Optional<Product> found = repo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Mouse Logitech", found.get().getName());
    }

    @Test
    void shouldReturnAllProducts() {
        repo.save(new Product(null, "Producto 1", 100.0));
        repo.save(new Product(null, "Producto 2", 200.0));
        repo.save(new Product(null, "Producto 3", 300.0));

        assertEquals(3, repo.findAll().size());
    }
}
