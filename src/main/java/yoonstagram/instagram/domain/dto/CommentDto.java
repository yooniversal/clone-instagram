package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class CommentDto {

    private Long id;
    private Long userId;

    @NotNull
    private Long postId;

    @NotBlank
    private String text;

    private String name;
    private String imageUrl;
}
