package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.service.PostService;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post/upload")
    public String upload(Model model) {

        String postDescription = "";
        model.addAttribute("postDescription", postDescription);
        return "post/upload";
    }

    @PostMapping("/post")
    public String uploadImages(@ModelAttribute("postDescription") String postDescription,
                               @RequestParam("uploadImgUrl") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.valueOf(userDetails.getUsername());
        postService.upload(postDescription, file, userId);

        return "post/upload";
    }
}
