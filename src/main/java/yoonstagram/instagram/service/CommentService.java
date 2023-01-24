package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonstagram.instagram.domain.Comment;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public Comment save(Long userId, Long postId, String content) {
        User user = userService.findOneById(userId);
        Post post = postService.findOneById(postId);
        Comment comment = new Comment(user, post, content);
        post.getComment().add(comment);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.delete(commentId);
    }
}
