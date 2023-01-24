package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Comment;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.PostDto;
import yoonstagram.instagram.domain.dto.PostUpdateDto;
import yoonstagram.instagram.domain.dto.SimpleUserDto;
import yoonstagram.instagram.domain.dto.UploadPostDto;
import yoonstagram.instagram.service.CommentService;
import yoonstagram.instagram.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

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
                             @RequestParam("uploadImgUrl") MultipartFile file,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.valueOf(userDetails.getUsername());
        postService.upload(uploadPostDto, file, userId);

        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }

    @GetMapping("/post/update/{postId}")
    public String updatePost(@PathVariable("postId") Long postId,
                             Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Post post = postService.findOneById(postId);
        PostDto postDto = new PostDto(post);
        model.addAttribute("postDto", postDto);

        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        return "post/update";
    }

    @PostMapping("/post/update")
    public String updatePost(PostUpdateDto postUpdateDto,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             RedirectAttributes redirectAttributes) {
        postService.update(postUpdateDto);
        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }

    @GetMapping("/post/popular")
    public String popular(Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        return "post/popular";
    }

    @GetMapping("/post/likes")
    public String likes(Model model,
                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
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

        SimpleUserDto currentUserDto = new SimpleUserDto(currentUser, true);
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

    @PostMapping("/post/cancel")
    public String updateCancel(RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }

    @PostMapping("/post/delete")
    public String delete(@RequestParam("postId") Long postId,
                         RedirectAttributes redirectAttributes,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Post post = postService.findOneById(postId);
        List<Comment> comments = post.getComment();

        // 댓글 삭제
        for(Comment comment : comments) {
            commentService.deleteComment(comment.getId());
        }

        // 게시물 삭제
        postService.delete(postId);
        redirectAttributes.addAttribute("id", principalDetails.getUser().getId());
        return "redirect:/user/profile";
    }

}
