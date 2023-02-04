package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.*;
import yoonstagram.instagram.domain.dto.NotificationDto;
import yoonstagram.instagram.domain.dto.PostInfoDto;
import yoonstagram.instagram.domain.dto.UserProfileDto;
import yoonstagram.instagram.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;
    private final LikeService likeService;
    private final NotificationService notificationService;

    @GetMapping("/user/profile")
    public String userProfile(@RequestParam Long id,
                              Model model,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        User findUser = userService.findOneById(id);
        int followerCount = followService.getFollowers(findUser.getId()).size();
        int followingCount = followService.getFollowings(findUser.getId()).size();
        boolean follow = followService.getFollowings(currentUser.getId()).contains(findUser);
        UserProfileDto findUserDto = new UserProfileDto(findUser, follow, followerCount, followingCount);
        model.addAttribute("findUserDto", findUserDto);

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        findUserDto.setLogin(currentUser.getId().equals(findUser.getId()));

        // 좋아요 게시물 처리
        List<Like> likes = likeService.findLikesWithUser(id);
        List<PostInfoDto> postDtos = new ArrayList<>();
        for (Like like : likes) {
            Post post = like.getPost();
            PostInfoDto postDto = new PostInfoDto();
            postDto.setId(post.getId());
            postDto.setText(post.getDescription());
            postDto.setTag(post.getTag());
            postDto.setPostImgUrl(post.getImageUrl());
            postDto.setLikeCount(post.getLikeCount());
            postDtos.add(postDto);
        }
        model.addAttribute("likePostDtos", postDtos);

        return "user/profile";
    }

    @GetMapping("/user/update")
    public String userUpdatePage(Model model,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();

        UserForm userForm = new UserForm();
        userForm.setId(currentUser.getId());
        userForm.setEmail(currentUser.getEmail());
        userForm.setUsername(currentUser.getUsername());
        userForm.setLink(currentUser.getLink());
        userForm.setDescription(currentUser.getDescription());
        userForm.setPhone(currentUser.getPhone());
        model.addAttribute("userForm", userForm);
        model.addAttribute("userImageUrl", currentUser.getImageUrl());

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());

        return "user/update";
    }

    @PostMapping("/user/update")
    public String userUpdate(@ModelAttribute("userForm") UserForm form,
                             @RequestParam(value = "imageUrl", required = false) MultipartFile file) {
        userService.updateProfile(form, file);
        return "redirect:/";
    }

    @GetMapping("user/follow")
    public String userFollow(Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User currentUser = principalDetails.getUser();
        List<User> currentUserFollowings = followService.getFollowings(currentUser.getId());
        List<User> users = userService.findUsers();
        List<UserProfileDto> userDtos = new ArrayList<>();
        for(User user : users) {
            if(user.getId().equals(currentUser.getId())) continue;

            boolean follow = currentUserFollowings.contains(user);
            int followerCount = followService.getFollowers(user.getId()).size();
            int followingCount = followService.getFollowings(user.getId()).size();
            userDtos.add(new UserProfileDto(user, follow, followerCount, followingCount));
        }

        model.addAttribute("users", userDtos);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        return "user/follow";
    }

    @GetMapping("user/notification")
    public String userNotification(Model model,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User currentUser = principalDetails.getUser();
        List<Notification> notifications = notificationService.notificationsWithUser(currentUser.getId());
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for(Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto(notification);
            notificationDtos.add(notificationDto);
        }

        model.addAttribute("notificationDtos", notificationDtos);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());

        return "user/notification";
    }

//    @DeleteMapping("/user/delete")
//    public String userDelete(@AuthenticationPrincipal PrincipalDetails principalDetails) {
//        userService.delete(principalDetails.getUser().getId());
//        return "redirect:/login";
//    }

    @GetMapping("/common")
    public String common(Model model,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        return "layout/common";
    }

}
