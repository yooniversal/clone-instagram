package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.domain.dto.UserForm;
import yoonstagram.instagram.domain.*;
import yoonstagram.instagram.repository.UserRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final PostService postService;
    private final FollowService followService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final NotificationService notificationService;

    @Transactional
    public Long join(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    public boolean isNameExist(String name) {
        List<User> findUsers = userRepository.findByName(name);
        if(!findUsers.isEmpty()) return true;
        return false;
    }

    public boolean isEmailExist(String email) {
        List<User> findUsers = userRepository.findByEmail(email);
        if(!findUsers.isEmpty()) return true;
        return false;
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public User findOneById(Long id) {
        return userRepository.findOneById(id);
    }

    public User findByName(String name) {
        List<User> findUsers = userRepository.findByName(name);
        return findUsers.get(0);
    }

    public List<User> findBySimilarName(String name) {
        List<User> findUsers = userRepository.findBySimilarName(name);
        return findUsers;
    }

    @Transactional
    public void update(Long id, String username) {
        User user = userRepository.findOneById(id);
        user.setUsername(username);
    }

    @Value("${profileImage.path}")
    private String uploadProfileFolder;

    @Transactional
    public void updateProfile(UserForm form, MultipartFile file) {
        User user = userRepository.findOneById(form.getId());

        if(file != null && !file.isEmpty()) { // 파일이 업로드 되었는지 확인
            String imageFileName = user.getId() + "_" + file.getOriginalFilename();
            Path imageFilePath = Paths.get(uploadProfileFolder + imageFileName);
            try {
                if (!user.getImageUrl().equals("null.jpg")) { // 이미 프로필 사진이 있을경우
                    File currentfile = new File(uploadProfileFolder + user.getImageUrl());
                    currentfile.delete(); // 원래파일 삭제
                }
                Files.write(imageFilePath, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setImageUrl(imageFileName);
        }

        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(form.getPassword()));
        user.setLink(form.getLink());
        user.setDescription(form.getDescription());
        user.setPhone(form.getPhone());
    }

    @Transactional
    public void delete(Long userId) {

        User currentUser = userRepository.findOneById(userId);

        // 팔로잉 정보 삭제
        List<User> followings = followService.getFollowings(userId);
        for (User following : followings) {
            Long followingUserId = following.getId();
            followService.unFollow(currentUser.getId(), followingUserId);
            notificationService.cancel(currentUser.getId(), NotificationStatus.FOLLOW, followingUserId);
        }

        // 팔로워 정보 삭제
        List<User> followers = followService.getFollowers(currentUser.getId());
        for (User follower : followers) {
            Long followerUserId = follower.getId();
            followService.unFollow(followerUserId, currentUser.getId());
            notificationService.cancel(followerUserId, NotificationStatus.FOLLOW, currentUser.getId());
        }

        // 좋아요 삭제
        List<Like> likes = likeService.findLikesWithUser(currentUser.getId());
        for (Like like : likes)
            likeService.unLikes(like.getPost().getId(), currentUser.getId());

        // 댓글 삭제
        List<Comment> comments = commentService.findCommentsWithUser(currentUser.getId());
        for (Comment comment : comments)
            commentService.deleteComment(comment.getId());

        // 알림 삭제
        notificationService.deleteWithUserId(currentUser.getId());

        // 게시물 삭제 관리
        List<Post> posts = postService.getPostsOfUser(currentUser.getId());
        for (Post post : posts) {
            List<Comment> postComments = post.getComment();

            // 댓글 삭제
            for(Comment comment : postComments) {
                commentService.deleteComment(comment.getId());
            }

            // 좋아요 삭제
            List<Like> postLikes = likeService.findLikesWithPostId(post.getId());
            for (Like like : postLikes) {
                likeService.unLikes(like.getPost().getId(), like.getUser().getId());
            }

            // 알림 삭제
            notificationService.deleteWithPostId(post.getId());

            // 게시물 삭제
            postService.delete(post.getId());
        }

        // 유저 삭제
        userRepository.delete(userId);
    }
}
