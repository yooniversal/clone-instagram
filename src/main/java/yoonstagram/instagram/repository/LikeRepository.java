package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;

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
        return em.createQuery("select l from Like l join l.user u where u.id = :id", Like.class)
                .setParameter("id", userId)
                .getResultList();
    }

    public List<Like> findLikesWithPostId(Long postId) {
        return em.createQuery("select l from Like l join l.post p where p.id = :postId", Like.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public Like findLike(long postId, Long userId) {
        return em.createQuery("select l " +
                                "from Like l " +
                                "join l.post p on p.id = :postId " +
                                "join l.user u on u.id = :userId",
                Like.class)
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
