package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.dto.PostInfoDto;
import yoonstagram.instagram.service.PostService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class PostApiController {

    private final PostService postService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> postInfo (
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("post id:{} user id:{}", postId, principalDetails.getUser().getId());
        return new ResponseEntity<>(postService.getPostInfoDto(postId, principalDetails.getUser().getId()), HttpStatus.OK);
//        return new ResponseEntity<>(postService.getPostInfoDto(postId, 1L), HttpStatus.OK);
    }

//    @PostMapping("/post/{postId}/likes")
//    public ResponseEntity<?> likes(@PathVariable long postId , @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        likesService.likes(postId, principalDetails.getUser().getId());
//        return new ResponseEntity<>("좋아요 성공", HttpStatus.OK);
//    }
//
//    @DeleteMapping("/post/{postId}/likes")
//    public ResponseEntity<?> unLikes(@PathVariable long postId , @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        likesService.unLikes(postId, principalDetails.getUser().getId());
//        return new ResponseEntity<>("좋아요 취소 성공", HttpStatus.OK);
//    }
//
//    @GetMapping("/post")
//    public ResponseEntity<?> mainStory(@AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size=3) Pageable pageable) {
//        return new ResponseEntity<>(postService.getPost(principalDetails.getUser().getId(), pageable), HttpStatus.OK);
//    }
//
    @GetMapping("/post/tag")
    public ResponseEntity<?> searchTag(@RequestParam String tag,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Post> postsWithTag = postService.getPostsWithTag(tag);
        List<PostInfoDto> postDtoList = new ArrayList<>();
        for(Post post : postsWithTag) {
            postDtoList.add(postService.getPostInfoDto(post.getId(), principalDetails.getUser().getId()));
        }
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }
//
//    @GetMapping("/post/likes")
//    public ResponseEntity<?> getLikesPost(@AuthenticationPrincipal PrincipalDetails principalDetails,
//                                          @PageableDefault(size=12) Pageable pageable) {
//        return new ResponseEntity<>(postService.getLikesPost(principalDetails.getUser().getId(), pageable), HttpStatus.OK);
//    }
//
//    @GetMapping("/post/popular")
//    public ResponseEntity<?> getPopularPost() {
//        return new ResponseEntity<>(postService.getPopularPost(), HttpStatus.OK);
//    }
}
