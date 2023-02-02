package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonstagram.instagram.domain.Follow;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.FollowRepository;
import yoonstagram.instagram.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void follow(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findOneById(fromUserId);
        User toUser = userRepository.findOneById(toUserId);
        followRepository.follow(fromUser, toUser);
    }

    @Transactional
    public void unFollow(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findOneById(fromUserId);
        User toUser = userRepository.findOneById(toUserId);
        followRepository.unfollow(fromUser, toUser);
    }

    public List<Follow> findAll() {
        return followRepository.findAll();
    }

    public List<User> getFollowers(Long userId) {
        return followRepository.getFollowers(userId);
    }

    public List<User> getFollowings(Long userId) {
        return followRepository.getFollowings(userId);
    }
}
