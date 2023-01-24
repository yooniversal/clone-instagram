package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PostUpdateDto {
    private long id;
    private String tag;
    private String text;
}
