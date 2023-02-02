package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonstagram.instagram.domain.Comment;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.CommentRepository;
import yoonstagram.instagram.repository.PostRepository;
import yoonstagram.instagram.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment save(Long userId, Long postId, String content) {
        User user = userRepository.findOneById(userId);
        Post post = postRepository.findOneById(postId);
        Comment comment = new Comment(user, post, content);
        post.getComment().add(comment);
        commentRepository.save(comment);

        return comment;
    }

    public List<Comment> findCommentsWithUser(Long userId) {
        return commentRepository.getCommentsOfUser(userId);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.delete(commentId);
    }

}
