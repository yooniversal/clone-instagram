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
        // 각 유저에서 팔로우 시 카운팅은 되는데 언팔 카운팅은 안됨!!
        // 아마 여기서 로직이 없어서 그런거같음
        // 팔로우 카운팅은 어떻게 되는건지도 알아보기
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
