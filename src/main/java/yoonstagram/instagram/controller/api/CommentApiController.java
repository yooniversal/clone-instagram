package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Comment;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.CommentDto;
import yoonstagram.instagram.service.CommentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentDto commentDto,
                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        Comment comment = commentService.save(user.getId(),
                commentDto.getPostId(),
                commentDto.getText());
        commentDto.setId(comment.getId());
        commentDto.setName(user.getName());
        commentDto.setImageUrl(user.getImageUrl());
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("댓글 삭제 성공", HttpStatus.OK);
    }

}
