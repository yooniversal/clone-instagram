package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yoonstagram.instagram.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }
    public Post findOneById(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    public List<Post> findWithTag(String tag) {
        return em.createQuery("select p from Post p where p.tag like concat('%', :tag, '%')", Post.class)
                .setParameter("tag", tag)
                .getResultList();
    }

    public List<Post> findByUserId(Long userId) {
        return em.createQuery("select p from Post p join p.user u on u.id = :userId", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void delete(Long postId) {
        em.createQuery("delete from Post p where p.id = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
        em.flush();
        em.clear();
    }

    public Long findPostCountWithTag(String tag) {
        try {
            return (Long) em.createQuery("select COUNT(p) from Post p where p.tag like concat('%', :tag, '%')")
                    .setParameter("tag", tag)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }
    }
}
