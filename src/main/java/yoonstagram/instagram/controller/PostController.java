package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.SimpleUserDto;
import yoonstagram.instagram.service.PostService;
import yoonstagram.instagram.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/post/popular")
    public String popular(Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        return "post/popular";
    }

    @GetMapping("/post/search/{tag}")
    public String searchTag(@PathVariable("tag") String tag,
                            Model model,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long currentUserId = principalDetails.getUser().getId();
        User currentUser = userService.findOneById(currentUserId);

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());

        SimpleUserDto currentUserDto = new SimpleUserDto(currentUser, true);
        model.addAttribute("currentUserDto", currentUserDto);
        model.addAttribute("tag", tag);

        List<Post> posts = postService.getPostsWithTag(tag);
        model.addAttribute("posts", posts);

        return "post/search";
    }

}
