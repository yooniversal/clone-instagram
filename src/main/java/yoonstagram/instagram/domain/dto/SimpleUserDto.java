package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.Setter;
import yoonstagram.instagram.domain.User;

@Getter @Setter
public class SimpleUserDto {

    private Long id;
    private String name;
    private String username;
    private String imageUrl;

    public SimpleUserDto(User user) {
        id = user.getId();
        name = user.getName();
        username = user.getUsername();
        imageUrl = user.getImageUrl();
    }
}
