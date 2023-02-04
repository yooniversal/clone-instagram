package yoonstagram.instagram.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import yoonstagram.instagram.domain.Follow;
import yoonstagram.instagram.domain.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

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
        em.flush();
        em.clear();
    }

    public List<Follow> findAll() {
        return em.createQuery("select f from Follow f", Follow.class)
                .getResultList();
    }

    public List<User> getFollowers(Long userId) {
        List<Follow> follows = em.createQuery("select f from Follow f " +
                        "join fetch f.fromUser fUser " +
                        "join fetch f.toUser tUser " +
                        "where tUser.id = :userId", Follow.class)
                .setParameter("userId", userId)
                .getResultList();

        return follows.stream()
                .map(Follow::getFromUser)
                .collect(Collectors.toList());
    }

    public List<User> getFollowings(Long userId) {
        List<Follow> follows = em.createQuery("select f from Follow f " +
                        "join fetch f.fromUser fUser " +
                        "join fetch f.toUser tUser " +
                        "where fUser.id = :userId", Follow.class)
                .setParameter("userId", userId)
                .getResultList();

        return follows.stream()
                .map(Follow::getToUser)
                .collect(Collectors.toList());
    }
}
