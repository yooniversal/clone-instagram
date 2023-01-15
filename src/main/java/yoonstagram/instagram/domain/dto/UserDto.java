package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yoonstagram.instagram.domain.User;

@Getter @Setter
@NoArgsConstructor
public class UserDto {
    private String password;
    private String name;
    private String username;

    public User toEntity() {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }
}
