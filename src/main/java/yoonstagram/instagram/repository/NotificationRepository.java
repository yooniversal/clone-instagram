package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yoonstagram.instagram.domain.Notification;
import yoonstagram.instagram.domain.NotificationStatus;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final EntityManager em;

    public void save(Notification notification) {
        em.persist(notification);
    }

    public Notification findOneById(Long id) {
        return em.find(Notification.class, id);
    }

    public List<Notification> notificationsWithUserId(Long userId) {
        return em.createQuery("select n from Notification n join User u on n.user.id = u.id " +
                "where u.id = :userId", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void cancel(Long userId, NotificationStatus status, Long targetId){
        if(status == NotificationStatus.LIKE) {
            em.createQuery("delete from Notification n " +
                            "where n.user.id = :userId and n.status = :status and n.postId = :postId")
                    .setParameter("userId", userId)
                    .setParameter("status", status)
                    .setParameter("postId", targetId)
                    .executeUpdate();
        } else {
            em.createQuery("delete from Notification n " +
                            "where n.user.id = :userId and n.status = :status and n.followId = :followId")
                    .setParameter("userId", userId)
                    .setParameter("status", status)
                    .setParameter("followId", targetId)
                    .executeUpdate();
        }

        em.flush();
        em.clear();
    }
}
