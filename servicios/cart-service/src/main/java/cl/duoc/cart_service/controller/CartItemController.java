package cl.duoc.cart_service.controller;

import cl.duoc.cart_service.dto.CartItemRequestDTO;
import cl.duoc.cart_service.dto.CartItemResponseDTO;
import cl.duoc.cart_service.model.CartItem;
import cl.duoc.cart_service.service.CartItemService;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart", description = "API de carrito de compras")
public class CartItemController {

    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los items", description = "Retorna todos los items del carrito")
    @ApiResponse(responseCode = "200", description = "Lista de items obtenida exitosamente")
    public ResponseEntity<List<CartItem>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener items por usuario", description = "Retorna todos los items del carrito de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Items del usuario obtenidos exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<List<CartItem>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Obtener detalle de item", description = "Retorna los detalles completos de un item del carrito")
    @ApiResponse(responseCode = "200", description = "Detalles del item obtenidos")
    @ApiResponse(responseCode = "404", description = "Item no encontrado")
    public ResponseEntity<CartItemResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithDetails(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar item", description = "Actualiza la cantidad o datos de un item del carrito")
    @ApiResponse(responseCode = "200", description = "Item actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Item no encontrado")
    public ResponseEntity<CartItem> update(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody CartItem item) {
        return ResponseEntity.ok(service.update(id, item));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar item", description = "Elimina un item del carrito")
    @ApiResponse(responseCode = "204", description = "Item eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Item no encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "Crear item", description = "Agrega un nuevo item al carrito")
    @ApiResponse(responseCode = "201", description = "Item creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartItem.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<CartItem> create(@Valid @org.springframework.web.bind.annotation.RequestBody CartItemRequestDTO dto) {
        CartItem item = new CartItem();
        item.setUserId(dto.getUserId());
        item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(item));
    }
}