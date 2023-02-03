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
        return em.createQuery("select n from Notification n " +
                        "join fetch n.user u " +
                        "where u.id = :userId", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void cancel(Long userId, NotificationStatus status, Long targetId){
        if(status == NotificationStatus.LIKE) {
            em.createQuery("delete from Notification n " +
                            "where n.fromUser.id = :userId and n.status = :status and n.postId = :postId")
                    .setParameter("userId", userId)
                    .setParameter("status", status)
                    .setParameter("postId", targetId)
                    .executeUpdate();
        } else {
            em.createQuery("delete from Notification n " +
                            "where n.user.id = :fromUserId and n.status = :status and n.fromUser.id = :toUserId")
                    .setParameter("fromUserId", targetId)
                    .setParameter("status", status)
                    .setParameter("toUserId", userId)
                    .executeUpdate();
        }

        em.flush();
        em.clear();
    }

    public void deleteWithPostId(Long postId) {
        em.createQuery("delete from Notification n where n.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
        em.flush();
        em.clear();
    }

    public void deleteWithUserId(Long userId) {
        em.createQuery("delete from Notification n where n.fromUser.id = :fromUserId")
                .setParameter("fromUserId", userId)
                .executeUpdate();
        em.createQuery("delete from Notification n where n.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
        em.flush();
        em.clear();
    }
}
