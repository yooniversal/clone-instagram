package yoonstagram.instagram.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.UserProfileDto;
import yoonstagram.instagram.service.PostService;
import yoonstagram.instagram.service.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/profile")
    public String userProfile(@RequestParam Long id,
                              Model model,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User findUser = userService.findOneById(id);
        UserProfileDto findUserDto = new UserProfileDto(findUser);
        model.addAttribute("findUserDto", findUserDto);

        User currentUser = principalDetails.getUser();
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("currentUserImageUrl", currentUser.getImageUrl());
        findUserDto.setLogin(currentUser.getId().equals(findUser.getId()));

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

}
