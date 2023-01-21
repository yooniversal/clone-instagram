package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.Setter;
import yoonstagram.instagram.domain.Comment;
import yoonstagram.instagram.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PostInfoDto {

    private long id;
    private String text;
    private String tag;
    private LocalDateTime date;
    private PostUploaderDto postUploader;
    private long likeCount;
    private boolean likeState;
    private boolean uploader;
    private String postImgUrl;
    private List<Comment> comments;
}
