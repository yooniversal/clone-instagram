package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.Setter;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;

@Getter @Setter
public class LikeDto {
    private Long postId;
    private Long userId;
    private String username;
    private String userImageUrl;
    private String postImageUrl;
    private Long likeCount;
    private boolean login;
    private boolean follow;

    public LikeDto(Like like) {
        Post post = like.getPost();
        User user = like.getUser();
        postId = post.getId();
        userId = user.getId();
        username = user.getName();
        userImageUrl = user.getImageUrl();
        postImageUrl = post.getImageUrl();
        likeCount = post.getLikeCount();
    }

    public LikeDto(Like like, boolean login, boolean follow) {
        Post post = like.getPost();
        User user = like.getUser();
        postId = post.getId();
        userId = user.getId();
        username = user.getName();
        userImageUrl = user.getImageUrl();
        postImageUrl = post.getImageUrl();
        likeCount = post.getLikeCount();
        this.login = login;
        this.follow = follow;
    }

    public LikeDto(Post post) {
        User user = post.getUser();
        postId = post.getId();
        userId = user.getId();
        username = user.getName();
        userImageUrl = user.getImageUrl();
        postImageUrl = post.getImageUrl();
        likeCount = post.getLikeCount();
        login = false;
        follow = false;
    }
}
