package yoonstagram.instagram.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserForm {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String link;
    private String description;
    private String phone;
}
