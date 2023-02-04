package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoonstagram.instagram.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<?> deleteComment(@PathVariable("userId") Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>("회원탈퇴 성공", HttpStatus.OK);
    }
}
