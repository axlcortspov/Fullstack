package cl.duoc.notification_service.service;

import cl.duoc.notification_service.model.Notification;
import cl.duoc.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public List<Notification> findAll() {
        return repository.findAll();
    }

    public List<Notification> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Notification create(Notification notification) {
        if (notification.getStatus() == null || notification.getStatus().isEmpty()) {
            notification.setStatus("Pendiente");
        }

        return repository.save(notification);
    }

    public Notification updateStatus(Long id, String status) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notification.setStatus(status);
        return repository.save(notification);
    }

    public Notification update(Long id, Notification notificationData) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notification.setUserId(notificationData.getUserId());
        notification.setMessage(notificationData.getMessage());
        notification.setType(notificationData.getType());
        notification.setStatus(notificationData.getStatus());

        return repository.save(notification);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}