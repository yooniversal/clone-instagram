package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Follower;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.UserProfileDto;
import yoonstagram.instagram.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

    @GetMapping({ "/", "/home" })
    public String home(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long currentUserId = Long.valueOf(userDetails.getUsername());

        log.info("currentUserId:{}", currentUserId);

        User currentUser = userService.findOneById(currentUserId);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        model.addAttribute("currentUsername", currentUser.getUsername());

        List<User> users = userService.findUsers();
        List<Follower> followers = currentUser.getFollowers();
        List<UserProfileDto> usersWithProfileDto = new ArrayList<>();
        for(User user : users) {
            if(user.getId().equals(currentUser.getId())) {
                continue;
            }

            boolean follow = followers.contains(user);

            usersWithProfileDto.add(new UserProfileDto(user));
        }

        model.addAttribute("users", usersWithProfileDto);

        return "home";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
