package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }
    public Like findOneById(Long id) {
        return em.find(Like.class, id);
    }

    public List<Like> findLikesWithUser(Long userId) {
        return em.createQuery("select l from Like l join l.user u where u.id = :id", Like.class)
                .setParameter("id", userId)
                .getResultList();
    }
}
