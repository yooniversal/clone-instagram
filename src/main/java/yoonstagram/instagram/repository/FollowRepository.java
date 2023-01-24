package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yoonstagram.instagram.domain.Follow;
import yoonstagram.instagram.domain.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final EntityManager em;

    public void follow(User fromUser, User toUser) {
        Follow follow = new Follow(fromUser, toUser);
        em.persist(follow);
    }

    public void unfollow(User fromUser, User toUser) {
        em.createQuery("delete from Follow f where f.fromUser = :fromUser and f.toUser = :toUser")
                .setParameter("fromUser", fromUser)
                .setParameter("toUser", toUser)
                .executeUpdate();
        em.clear();
    }

    public List<Follow> findAll() {
        return em.createQuery("select f from Follow f", Follow.class)
                .getResultList();
    }

    public List<User> getFollowers(Long userId) {
        return em.createQuery("select f.fromUser from Follow f where f.toUser.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<User> getFollowings(Long userId) {
        return em.createQuery("select f.toUser from Follow f where f.fromUser.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<User> getFollows(Long userId) {
        return em.createQuery("select f from Follow f where f.fromUser.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
