package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.PostRepository;
import yoonstagram.instagram.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Value("${post.path}")
    private String uploadPostFolder;

    @Transactional
    public Long upload(String postDescription, MultipartFile file, Long userId) {
        User user = userRepository.findOneById(userId);

        String imageFileName = user.getId() + "_" + file.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadPostFolder + imageFileName);
        try {
            Files.write(imageFilePath, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Post post = Post.createPost(imageFileName, postDescription, user);
        postRepository.save(post);

        return post.getId();
    }
}
