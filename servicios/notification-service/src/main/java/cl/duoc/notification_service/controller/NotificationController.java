package cl.duoc.notification_service.controller;

import cl.duoc.notification_service.dto.NotificationResponseDTO;
import cl.duoc.notification_service.model.Notification;
import cl.duoc.notification_service.service.NotificationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "API de notificaciones de usuarios")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones", description = "Retorna todas las notificaciones")
    @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas exitosamente")
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener notificaciones de usuario", description = "Retorna todas las notificaciones de un usuario")
    @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    public ResponseEntity<List<Notification>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Obtener detalle de notificación", description = "Retorna los detalles completos de una notificación")
    @ApiResponse(responseCode = "200", description = "Detalle obtenido")
    @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    public ResponseEntity<NotificationResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithUser(id));
    }

    @PostMapping
    @Operation(summary = "Crear notificación", description = "Crea una nueva notificación")
    @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Notification.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(notification));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar notificación", description = "Actualiza una notificación existente")
    @ApiResponse(responseCode = "200", description = "Notificación actualizada")
    @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    public ResponseEntity<Notification> update(
            @PathVariable Long id,
            @RequestBody Notification notification
    ) {
        return ResponseEntity.ok(service.update(id, notification));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de notificación", description = "Cambia el estado de una notificación")
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    public ResponseEntity<Notification> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación")
    @ApiResponse(responseCode = "204", description = "Notificación eliminada")
    @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}