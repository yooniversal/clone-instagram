package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yoonstagram.instagram.domain.Comment;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOneById(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> getCommentsOfUser(Long userId) {
        return em.createQuery("select c from Comment c where c.userId = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void delete(Long commentId) {
        em.createQuery("delete from Comment c where c.id = :commentId")
                .setParameter("commentId", commentId)
                .executeUpdate();
        em.flush();
        em.clear();
    }

    public List<Comment> getCommentsOfPost(Long postId) {
        return em.createQuery("select c from Comment c join c.post p on p.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
