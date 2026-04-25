package cl.duoc.product_service.service;

import java.util.List;
import org.springframework.stereotype.Service;
import cl.duoc.product_service.model.Product;
import cl.duoc.product_service.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product save(Product product) {
        return repo.save(product);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}