package cl.duoc.notification_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@Schema(name = "Notification", description = "Modelo de notificación de usuario")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la notificación", example = "1")
    private Long id;

    @Schema(description = "ID del usuario receptor", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "Contenido del mensaje", example = "Tu pedido ha sido enviado", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "Tipo de notificación", example = "ORDER_UPDATE", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "Estado de la notificación", example = "UNREAD")
    private String status;

}
