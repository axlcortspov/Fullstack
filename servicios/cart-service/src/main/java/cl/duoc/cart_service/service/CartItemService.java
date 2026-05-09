package cl.duoc.cart_service.service;

import cl.duoc.cart_service.client.ProductClient;
import cl.duoc.cart_service.client.UserClient;
import cl.duoc.cart_service.dto.CartItemResponseDTO;
import cl.duoc.cart_service.dto.ProductDTO;
import cl.duoc.cart_service.dto.UserDTO;
import cl.duoc.cart_service.exception.CartItemNotFoundException;
import cl.duoc.cart_service.exception.ProductNotFoundException;
import cl.duoc.cart_service.exception.ServiceUnavailableException;
import cl.duoc.cart_service.exception.UserNotFoundException;
import cl.duoc.cart_service.model.CartItem;
import cl.duoc.cart_service.repository.CartItemRepository;
import feign.FeignException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartItemService {

    private final CartItemRepository repository;
    private final UserClient userClient;
    private final ProductClient productClient;

    public CartItemService(CartItemRepository repository,
                           UserClient userClient,
                           ProductClient productClient) {
        this.repository = repository;
        this.userClient = userClient;
        this.productClient = productClient;
    }

    public List<CartItem> findAll() {
        return repository.findAll();
    }

    public List<CartItem> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public CartItem save(CartItem item) {
        log.info("[cart-service] Validando usuario id={} en user-service", item.getUserId());
        try {
            UserDTO user = userClient.getUserById(item.getUserId());
            log.info("[cart-service] Usuario validado: {}", user.getUsername());
        } catch (FeignException.NotFound ex) {
            log.warn("[cart-service] Usuario id={} no existe en user-service", item.getUserId());
            throw new UserNotFoundException(item.getUserId());
        } catch (FeignException ex) {
            log.error("[cart-service] user-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("user-service");
        }

        log.info("[cart-service] Validando producto id={} en product-service", item.getProductId());
        try {
            ProductDTO product = productClient.getProductById(item.getProductId());
            log.info("[cart-service] Producto validado: {}", product.getName());
        } catch (FeignException.NotFound ex) {
            log.warn("[cart-service] Producto id={} no existe en product-service", item.getProductId());
            throw new ProductNotFoundException(item.getProductId());
        } catch (FeignException ex) {
            log.error("[cart-service] product-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("product-service");
        }

        CartItem saved = repository.save(item);
        log.info("[cart-service] CartItem creado con id={}", saved.getId());
        return saved;
    }

    public CartItemResponseDTO findByIdWithDetails(Long id) {
        CartItem item = repository.findById(id)
            .orElseThrow(() -> new CartItemNotFoundException(id));

        log.info("[cart-service] Obteniendo detalles para cartItem id={}", id);

        UserDTO user;
        try {
            user = userClient.getUserById(item.getUserId());
            log.info("[cart-service] Usuario obtenido para cartItem id={}", id);
        } catch (FeignException.NotFound ex) {
            log.warn("[cart-service] Usuario id={} no encontrado al obtener detalles de cartItem id={}", item.getUserId(), id);
            throw new UserNotFoundException(item.getUserId());
        } catch (FeignException ex) {
            log.error("[cart-service] user-service no disponible al buscar cartItem id={}", id);
            throw new ServiceUnavailableException("user-service");
        }

        ProductDTO product;
        try {
            product = productClient.getProductById(item.getProductId());
            log.info("[cart-service] Producto obtenido para cartItem id={}", id);
        } catch (FeignException.NotFound ex) {
            log.warn("[cart-service] Producto id={} no encontrado al obtener detalles de cartItem id={}", item.getProductId(), id);
            throw new ProductNotFoundException(item.getProductId());
        } catch (FeignException ex) {
            log.error("[cart-service] product-service no disponible al buscar cartItem id={}", id);
            throw new ServiceUnavailableException("product-service");
        }

        return new CartItemResponseDTO(item.getId(), item.getQuantity(), user, product);
    }

    public CartItem update(Long id, CartItem item) {
        CartItem existing = repository.findById(id)
            .orElseThrow(() -> new CartItemNotFoundException(id));

        existing.setUserId(item.getUserId());
        existing.setProductId(item.getProductId());
        existing.setQuantity(item.getQuantity());

        log.info("[cart-service] CartItem id={} actualizado", id);
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CartItemNotFoundException(id);
        }
        repository.deleteById(id);
        log.info("[cart-service] CartItem id={} eliminado", id);
    }
}