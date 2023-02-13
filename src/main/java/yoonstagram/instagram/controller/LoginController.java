package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yoonstagram.instagram.domain.dto.LoginUserDto;
import yoonstagram.instagram.domain.dto.UserDto;
import yoonstagram.instagram.service.UserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value="error", required = false) String error,
                        Model model) {
        model.addAttribute("LoginUserDto", new LoginUserDto());
        return "login";
    }

    @GetMapping("/signUp")
    public String signUp(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@Valid UserDto userDto, BindingResult result) {
        if(result.hasErrors()) {
            return "signUp";
        }

        if (userService.isNameExist(userDto.getName())) {
            result.rejectValue("name", "name.exist", "같은 이름이 존재합니다.");
            return "signUp";
        }

        if (userService.isEmailExist(userDto.getEmail())) {
            result.rejectValue("email", "email.exist", "같은 이메일이 존재합니다.");
            return "signUp";
        }

        userService.join(userDto.toEntity());
        return "redirect:/login";
    }
}
