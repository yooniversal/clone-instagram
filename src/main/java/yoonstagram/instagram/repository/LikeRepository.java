package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yoonstagram.instagram.domain.Like;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final EntityManager em;

    public void save(Like like) {
        em.persist(like);
    }

    public Like findOneById(Long id) {
        return em.find(Like.class, id);
    }

    public List<Like> findLikesWithUser(Long userId) {
        return em.createQuery("select l from Like l " +
                        "join fetch l.user u " +
                        "where u.id = :userId", Like.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Like> findLikesWithPostId(Long postId) {
        return em.createQuery("select l from Like l " +
                        "join fetch l.post p " +
                        "where p.id = :postId", Like.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public Like findLike(long postId, Long userId) {
        return em.createQuery("select l from Like l " +
                                "join fetch l.post p " +
                                "join fetch l.user u " +
                                "where p.id = :postId and u.id = :userId", Like.class)
                .setParameter("postId", postId)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    public void deleteLike(Long likeId) {
        em.createQuery("delete from Like l where l.id = :likeId")
                .setParameter("likeId", likeId)
                .executeUpdate();
        em.flush();
        em.clear();
    }

}
