package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.LikeRepository;
import yoonstagram.instagram.repository.NotificationRepository;
import yoonstagram.instagram.repository.PostRepository;
import yoonstagram.instagram.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public List<Like> findLikesWithUser(Long userId) {
        return likeRepository.findLikesWithUser(userId);
    }

    public List<Like> findLikesWithPostId(Long postId) {
        return likeRepository.findLikesWithPostId(postId);
    }

    @Transactional
    public void likes(long postId, Long userId) {
        Post post = postRepository.findOneById(postId);
        User user = userRepository.findOneById(userId);
        Like like = new Like(post, user);

        post.getLikes().add(like);
        post.setLikeCount(post.getLikeCount() + 1);
        likeRepository.save(like);
    }

    @Transactional
    public void unLikes(long postId, Long userId) {
        Like like = likeRepository.findLike(postId, userId);
        Post post = postRepository.findOneById(postId);
        post.setLikeCount(post.getLikeCount() - 1);
        likeRepository.deleteLike(like.getId());
    }

}
