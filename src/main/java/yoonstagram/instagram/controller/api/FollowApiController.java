package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.service.FollowService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") Long userId,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        followService.follow(principalDetails.getUser().getId(), userId);
        return new ResponseEntity<>("팔로우 성공", HttpStatus.OK);
    }

    @DeleteMapping("/follow/{userId}")
    public ResponseEntity<?> unFollowUser(@PathVariable("userId") Long userId,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("delete follow id:{}", userId);
        followService.unFollow(principalDetails.getUser().getId(), userId);
        return new ResponseEntity<>("팔로우 취소 성공", HttpStatus.OK);
    }
}