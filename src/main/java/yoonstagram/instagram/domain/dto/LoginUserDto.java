package yoonstagram.instagram.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginUserDto {

    @NotEmpty(message = "ID 입력은 필수입니다")
    private String name;

    @NotEmpty
    private String password;
}
