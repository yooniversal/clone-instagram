package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.SimpleUserDto;
import yoonstagram.instagram.domain.dto.UploadPostDto;
import yoonstagram.instagram.service.LikeService;
import yoonstagram.instagram.service.PostService;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    @GetMapping("/post/upload")
    public String upload(Model model,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {

        UploadPostDto uploadPostDto = new UploadPostDto();
        model.addAttribute("UploadPostDto", uploadPostDto);

        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());

        return "post/upload";
    }

    @PostMapping("/post")
    public String uploadPost(@ModelAttribute("UploadPostDto") UploadPostDto uploadPostDto,
                               @RequestParam("uploadImgUrl") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.valueOf(userDetails.getUsername());
        postService.upload(uploadPostDto, file, userId);

        return "post/upload";
    }

    @GetMapping("/post/popular")
    public String popular(Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Post> posts = postService.getAllPosts();
        Collections.sort(posts);
        model.addAttribute("posts", posts);

        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());

        return "post/popular";
    }

    @GetMapping("/post/likes")
    public String likes(Model model,
                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        List<Like> likes = likeService.findLikesWithUser(currentUser.getId());
        model.addAttribute("likes", likes);

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());

        return "post/likes";
    }

    @GetMapping("/post/search")
    public String searchTag(@RequestParam("tag") String tag,
                            Model model,
                            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());

        SimpleUserDto currentUserDto = new SimpleUserDto(currentUser);
        model.addAttribute("currentUserDto", currentUserDto);
        model.addAttribute("tag", tag);

        List<Post> posts = postService.getPostsWithTag(tag);
        model.addAttribute("posts", posts);

        return "post/search";
    }

    @PostMapping("/post/searchForm")
    public String searchForm(String tag,
                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("tag", tag);
        return "redirect:/post/search";
    }

}
