package yoonstagram.instagram.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PostUploaderDto {
    private Long id;
    private String name;
    private String profileImgUrl;
}
