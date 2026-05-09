package cl.duoc.notification_service.controller;

import cl.duoc.notification_service.dto.NotificationResponseDTO;
import cl.duoc.notification_service.model.Notification;
import cl.duoc.notification_service.service.NotificationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    // Endpoint enriquecido: notificación con datos del usuario vía Feign
    @GetMapping("/{id}/detail")
    public ResponseEntity<NotificationResponseDTO> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByIdWithUser(id));
    }

    // Al crear, valida que el usuario exista en user-service
    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(notification));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(
            @PathVariable Long id,
            @RequestBody Notification notification
    ) {
        return ResponseEntity.ok(service.update(id, notification));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Notification> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}