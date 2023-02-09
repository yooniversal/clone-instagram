package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.UserProfileDto;
import yoonstagram.instagram.service.FollowService;
import yoonstagram.instagram.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final FollowService followService;

    @GetMapping({ "/", "/home" })
    public String home(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long currentUserId = Long.valueOf(userDetails.getUsername());

        User currentUser = userService.findOneById(currentUserId);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        model.addAttribute("currentUsername", currentUser.getUsername());

        List<User> users = userService.findUsers();
        List<User> currentUserFollowings = followService.getFollowings(currentUserId);
        List<UserProfileDto> usersWithProfileDto = new ArrayList<>();
        for(User user : users) {
            if(user.getId().equals(currentUser.getId())) {
                continue;
            }

            boolean follow = currentUserFollowings.contains(user);
            int followerCount = followService.getFollowers(user.getId()).size();
            int followingCount = followService.getFollowings(user.getId()).size();
            usersWithProfileDto.add(new UserProfileDto(user, follow, followerCount, followingCount));
        }

        model.addAttribute("users", usersWithProfileDto);

        return "home";
    }

}
