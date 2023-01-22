package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.PostInfoDto;
import yoonstagram.instagram.domain.dto.PostUploaderDto;
import yoonstagram.instagram.domain.dto.UploadPostDto;
import yoonstagram.instagram.repository.PostRepository;
import yoonstagram.instagram.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

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
        postInfoDto.setComments(post.getComment());

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

        return postInfoDto;
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
}
