package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.Setter;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;

@Getter @Setter
public class LikeDto {
    private Long postId;
    private String postImageUrl;
    private int likeCount;

    public LikeDto(Like like) {
        Post post = like.getPost();
        postId = post.getId();
        postImageUrl = post.getImageUrl();
        likeCount = post.getLikeCount();
    }

    public LikeDto(Post post) {
        postId = post.getId();
        postImageUrl = post.getImageUrl();
        likeCount = post.getLikeCount();
    }
}
