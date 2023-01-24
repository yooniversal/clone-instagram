package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yoonstagram.instagram.domain.Post;

@Getter @Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String text;
    private String tag;
    private String postImgUrl;

    public PostDto(Post post) {
        id = post.getId();
        text = post.getDescription();
        tag = post.getTag();
        postImgUrl = post.getImageUrl();
    }
}
