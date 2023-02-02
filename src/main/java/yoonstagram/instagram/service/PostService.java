package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.domain.Comment;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.*;
import yoonstagram.instagram.repository.CommentRepository;
import yoonstagram.instagram.repository.PostRepository;
import yoonstagram.instagram.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Value("${post.path}")
    private String uploadPostFolder;

    @Transactional
    public Long upload(UploadPostDto uploadPostDto, MultipartFile file, Long userId) {
        User user = userRepository.findOneById(userId);

        String imageFileName = user.getId() + "_" + file.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadPostFolder + imageFileName);
        try {
            Files.write(imageFilePath, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Post post = Post.createPost(imageFileName, uploadPostDto.getTag(), uploadPostDto.getText(), user);
        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public PostInfoDto getPostInfoDto(Long postId, Long userId) {
        PostInfoDto postInfoDto = new PostInfoDto();
        postInfoDto.setId(postId);

        Post post = postRepository.findOneById(postId);
        postInfoDto.setTag(post.getTag());
        postInfoDto.setText(post.getDescription());
        postInfoDto.setPostImgUrl(post.getImageUrl());
        postInfoDto.setDate(post.getDate());

        // 현재 user의 해당 post에 대한 정보
        postInfoDto.setLikeCount(post.getLikes().size());
        List<Like> likes = post.getLikes();
        for(Like like : likes)
            if(like.getUser().getId().equals(userId)) postInfoDto.setLikeState(true);

        // 해당 post 작성 user 정보 설정
        User user = userRepository.findOneById(post.getUser().getId());
        postInfoDto.setPostUploader(new PostUploaderDto(user.getId(), user.getName(), user.getImageUrl()));

        if(userId.equals(post.getUser().getId())) postInfoDto.setUploader(true);
        else postInfoDto.setUploader(false);

        // 해당 post의 comments 설정
        List<Comment> comments = commentRepository.getCommentsOfPost(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        for(Comment comment : comments) {
            CommentDto commentDto = new CommentDto();
            User commentUser = userRepository.findOneById(comment.getUserId());
            commentDto.setId(comment.getId());
            commentDto.setUserId(commentUser.getId());
            commentDto.setPostId(postId);
            commentDto.setName(commentUser.getName());
            commentDto.setText(comment.getContent());
            commentDto.setImageUrl(commentUser.getImageUrl());
            commentDtos.add(commentDto);
        }
        postInfoDto.setComments(commentDtos);

        return postInfoDto;
    }

    public PostInfoDto getBlankPostInfoDto(User user) {
        PostInfoDto postInfoDto = new PostInfoDto();
        postInfoDto.setTag("");
        postInfoDto.setText("");
        postInfoDto.setPostImgUrl("");

        // 해당 post 작성 user 정보 설정
        postInfoDto.setPostUploader(new PostUploaderDto(user.getId(), user.getName(), user.getImageUrl()));
        postInfoDto.setUploader(true);

        return postInfoDto;
    }

    public Post findOneById(Long id) {
        return postRepository.findOneById(id);
    }

    public List<Post> getPostsOfUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsWithTag(String tag) {
        return postRepository.findWithTag(tag);
    }

    public Long getPostCountWithTag(String tag) {
        return postRepository.findPostCountWithTag(tag);
    }

    @Transactional
    public void update(PostUpdateDto postUpdateDto) {
        Post post = postRepository.findOneById(postUpdateDto.getId());
        post.setTag(postUpdateDto.getTag());
        post.setDescription(postUpdateDto.getText());
    }

    @Transactional
    public void delete(Long postId) {
        postRepository.delete(postId);
    }
}
