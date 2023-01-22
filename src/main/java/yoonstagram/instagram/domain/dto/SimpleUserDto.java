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
    private boolean follow;
    private boolean loginUser;

    public SimpleUserDto(User user, boolean loginUser) {
        id = user.getId();
        name = user.getName();
        username = user.getUsername();
        imageUrl = user.getImageUrl();
        follow = false;
        this.loginUser = loginUser;
    }

    public SimpleUserDto(User user, boolean follow, boolean loginUser) {
        id = user.getId();
        name = user.getName();
        username = user.getUsername();
        imageUrl = user.getImageUrl();
        this.follow = follow;
        this.loginUser = loginUser;
    }
}
