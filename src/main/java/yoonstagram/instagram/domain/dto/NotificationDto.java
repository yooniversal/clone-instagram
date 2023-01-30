package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.Setter;
import yoonstagram.instagram.domain.Notification;

import java.time.LocalDateTime;

@Getter @Setter
public class NotificationDto {
    private Long id;
    private Long postId;
    private Long userId;
    private Long fromUserId;
    private String fromUserName;
    private String fromUserImageUrl;
    private String postImageUrl;
    private LocalDateTime time;
    private String status;

    public NotificationDto(Notification notification) {
        id = notification.getId();
        postId = notification.getPostId();
        userId = notification.getUser().getId();
        fromUserId = notification.getFromUser().getId();
        fromUserName = notification.getFromUser().getName();
        fromUserImageUrl = notification.getFromUser().getImageUrl();
        postImageUrl = notification.getImageUrl();
        time = notification.getTime();
        status = notification.getStatus().toString();
    }
}
