package cl.duoc.notification_service.service;

import cl.duoc.notification_service.client.UserClient;
import cl.duoc.notification_service.dto.NotificationResponseDTO;
import cl.duoc.notification_service.dto.UserDTO;
import cl.duoc.notification_service.exception.NotificationNotFoundException;
import cl.duoc.notification_service.exception.ServiceUnavailableException;
import cl.duoc.notification_service.exception.UserNotFoundException;
import cl.duoc.notification_service.model.Notification;
import cl.duoc.notification_service.repository.NotificationRepository;
import feign.FeignException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final UserClient userClient;

    public NotificationService(NotificationRepository repository, UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    public List<Notification> findAll() {
        return repository.findAll();
    }

    public List<Notification> findByUserId(Long userId) {
        validateUser(userId);
        return repository.findByUserId(userId);
    }

    public Notification create(Notification notification) {
        log.info("[notification-service] Validando usuario id={} en user-service", notification.getUserId());

        UserDTO user = validateUser(notification.getUserId());

        if (notification.getStatus() == null || notification.getStatus().isEmpty()) {
            notification.setStatus("PENDING");
        }

        Notification saved = repository.save(notification);

        log.info("[notification-service] Notificación creada con id={} para usuario={}",
                saved.getId(), user.getName());

        return saved;
    }

    public NotificationResponseDTO findByIdWithUser(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        log.info("[notification-service] Obteniendo usuario id={} para notificación id={}",
                notification.getUserId(), id);

        UserDTO user = validateUser(notification.getUserId());

        log.info("[notification-service] Usuario obtenido para notificación id={}", id);

        return new NotificationResponseDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getType(),
                notification.getStatus(),
                user
        );
    }

    public Notification update(Long id, Notification notificationData) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        validateUser(notificationData.getUserId());

        notification.setUserId(notificationData.getUserId());
        notification.setMessage(notificationData.getMessage());
        notification.setType(notificationData.getType());
        notification.setStatus(notificationData.getStatus());

        log.info("[notification-service] Notificación id={} actualizada", id);

        return repository.save(notification);
    }

    public Notification updateStatus(Long id, String status) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        notification.setStatus(status);

        log.info("[notification-service] Notificación id={} actualizada a status={}", id, status);

        return repository.save(notification);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotificationNotFoundException(id);
        }

        repository.deleteById(id);

        log.info("[notification-service] Notificación id={} eliminada", id);
    }

    private UserDTO validateUser(Long userId) {
        try {
            log.info("[notification-service] Consultando user-service para usuario id={}", userId);

            UserDTO user = userClient.getUserById(userId);

            log.info("[notification-service] Usuario encontrado: id={}, name={}",
                    user.getId(), user.getName());

            return user;

        } catch (FeignException.NotFound ex) {
            log.warn("[notification-service] Usuario id={} no encontrado en user-service", userId);
            throw new UserNotFoundException(userId);

        } catch (FeignException ex) {
            log.error("[notification-service] user-service no disponible: {}", ex.getMessage());
            throw new ServiceUnavailableException("user-service");
        }
    }
}