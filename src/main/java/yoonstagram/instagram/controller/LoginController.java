package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.LoginUserDto;
import yoonstagram.instagram.domain.dto.UserDto;
import yoonstagram.instagram.service.UserService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(value="error", required = false) String error,
                        Model model) {
        model.addAttribute("LoginUserDto", new LoginUserDto());
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute("LoginUserDto") LoginUserDto loginUserDto) {
//        User findUser = userService.findByName(loginUserDto.getName());
//        if(!findUser.getPassword().equals(bCryptPasswordEncoder.encode(loginUserDto.getPassword()))) {
//            return "login";
//        }
//
//        return "hello";
//    }

    @GetMapping("/signUp")
    public String signUp(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute("userDto") UserDto userDto) {
        userService.join(userDto.toEntity());
        return "redirect:/login";
    }
}
