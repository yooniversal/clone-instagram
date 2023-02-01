package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.*;
import yoonstagram.instagram.domain.dto.*;
import yoonstagram.instagram.service.CommentService;
import yoonstagram.instagram.service.LikeService;
import yoonstagram.instagram.service.NotificationService;
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
    private final CommentService commentService;
    private final NotificationService notificationService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> postInfo (@PathVariable("postId") Long postId,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new ResponseEntity<>(postService.getPostInfoDto(postId, principalDetails.getUser().getId()), HttpStatus.OK);
    }

    @PostMapping("/post/{postId}/likes")
    public ResponseEntity<?> likes(@PathVariable("postId") Long postId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        likeService.likes(postId, currentUser.getId());

        Post post = postService.findOneById(postId);
        if(!post.getUser().getId().equals(currentUser.getId()))
            notificationService.save(post.getUser(), currentUser, post.getImageUrl(), NotificationStatus.LIKE, postId);

        return new ResponseEntity<>("좋아요 성공", HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}/likes")
    public ResponseEntity<?> unLikes(@PathVariable("postId") Long postId,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        likeService.unLikes(postId, currentUser.getId());

        Post post = postService.findOneById(postId);
        if(!post.getUser().getId().equals(currentUser.getId()))
            notificationService.cancel(currentUser.getId(), NotificationStatus.LIKE, postId);

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
        for (Post post : posts) {
            LikeDto likeDto = new LikeDto(post);
            likeDtos.add(likeDto);
        }

        return new ResponseEntity<>(likeDtos, HttpStatus.OK);
    }

    @GetMapping("/upload")
    public ResponseEntity<?> uploadInfo (
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return new ResponseEntity<>(postService.getBlankPostInfoDto(principalDetails.getUser()), HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPost(@RequestParam("uploadText") String text,
                                        @RequestParam("uploadTag") String tag,
                                        @RequestParam("uploadImgUrl") MultipartFile file,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();

        UploadPostDto uploadPostDto = new UploadPostDto();
        uploadPostDto.setText(text);
        uploadPostDto.setTag(tag);

        postService.upload(uploadPostDto, file, user.getId());

        return new ResponseEntity<>("업로드 성공", HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editPost(@RequestParam("postId") Long postId,
                                      @RequestParam("text") String text,
                                      @RequestParam("tag") String tag) {
        PostUpdateDto postUpdateDto = new PostUpdateDto();
        postUpdateDto.setId(postId);
        postUpdateDto.setText(text);
        postUpdateDto.setTag(tag);

        postService.update(postUpdateDto);

        return new ResponseEntity<>("업데이트 성공", HttpStatus.OK);
    }

    @DeleteMapping("/post/delete/{postId}")
    public ResponseEntity<?> postDelete(@PathVariable("postId") Long postId) {

        log.info("postDelete call@@ postId:{}", postId);

        Post post = postService.findOneById(postId);
        List<Comment> comments = post.getComment();

        // 댓글 삭제
        for(Comment comment : comments) {
            commentService.deleteComment(comment.getId());
        }

        // 좋아요 삭제
        List<Like> likes = post.getLikes();
        for (Like like : likes) {
            likeService.unLikes(like.getPost().getId(), like.getUser().getId());
        }

        // 게시물 삭제
        postService.delete(postId);

        log.info("삭제됨");

        return new ResponseEntity<>("게시물 삭제 성공", HttpStatus.OK);
    }

}
