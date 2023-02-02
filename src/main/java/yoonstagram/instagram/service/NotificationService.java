package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonstagram.instagram.domain.Notification;
import yoonstagram.instagram.domain.NotificationStatus;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void save(User user, User fromUser, String imageUrl, NotificationStatus status, Long postId) {
        Notification notification = new Notification(user, fromUser, imageUrl, status, postId);
        notificationRepository.save(notification);
    }

    public List<Notification> notificationsWithUser(Long userId) {
        return notificationRepository.notificationsWithUserId(userId);
    }

    @Transactional
    public void cancel(Long userId, NotificationStatus status, Long targetId) {
        notificationRepository.cancel(userId, status, targetId);
    }

    @Transactional
    public void deleteWithPostId(Long postId) {
        notificationRepository.deleteWithPostId(postId);
    }

    @Transactional
    public void deleteWithUserId(Long userId) {
        notificationRepository.deleteWithUserId(userId);
    }
}
