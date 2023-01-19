package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yoonstagram.instagram.domain.User;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
public class UserDto {

    private String name;
    private String password;
    private String email;
    private String phone;

    public User toEntity() {
        User user = new User();
        user.setName(name);
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);

        return user;
    }
}
