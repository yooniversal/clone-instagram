package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.LikeDto;
import yoonstagram.instagram.domain.dto.PostInfoDto;
import yoonstagram.instagram.service.LikeService;
import yoonstagram.instagram.service.PostService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class PostApiController {

    private final PostService postService;
    private final LikeService likeService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> postInfo (
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new ResponseEntity<>(postService.getPostInfoDto(postId, principalDetails.getUser().getId()), HttpStatus.OK);
    }

    @PostMapping("/post/{postId}/likes")
    public ResponseEntity<?> likes(@PathVariable("postId") Long postId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.likes(postId, principalDetails.getUser().getId());
        return new ResponseEntity<>("좋아요 성공", HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}/likes")
    public ResponseEntity<?> unLikes(@PathVariable("postId") Long postId,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likeService.unLikes(postId, principalDetails.getUser().getId());
        return new ResponseEntity<>("좋아요 취소 성공", HttpStatus.OK);
    }

    @GetMapping("/post")
    public ResponseEntity<?> mainStory(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Post> posts = postService.getPostsOfUser(principalDetails.getUser().getId());
        List<PostInfoDto> postInfoDtos = new ArrayList<>();
        for(Post post : posts)
            postInfoDtos.add(postService.getPostInfoDto(post.getId(), principalDetails.getUser().getId()));

        return new ResponseEntity<>(postInfoDtos, HttpStatus.OK);
    }

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

    @GetMapping("/post/likes")
    public ResponseEntity<?> getLikesPost(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        List<Like> likes = likeService.findLikesWithUser(currentUser.getId());
        List<LikeDto> likeDtos = new ArrayList<>();
        for(Like like : likes) {
            LikeDto likeDto = new LikeDto(like);
            likeDtos.add(likeDto);
        }
        return new ResponseEntity<>(likeDtos, HttpStatus.OK);
    }

    @GetMapping("/post/popular")
    public ResponseEntity<?> getPopularPost() {
        List<Post> posts = postService.getAllPosts();
        Collections.sort(posts);

        List<LikeDto> likeDtos = new ArrayList<>();
        for(Post post : posts) {
            LikeDto likeDto = new LikeDto(post);
            likeDtos.add(likeDto);
        }

        return new ResponseEntity<>(likeDtos, HttpStatus.OK);
    }
}
