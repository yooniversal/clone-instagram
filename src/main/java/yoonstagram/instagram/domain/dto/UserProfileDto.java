package yoonstagram.instagram.domain.dto;

import lombok.*;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class UserProfileDto {
    private Long id;
    private String name;
    private String username;
    private String imageUrl;
    private String description;
    private String link;
    private String email;
    private boolean follow;
    private int followerCount;
    private int followingCount;
    private List<Post> posts;
    private int postCount;
    private boolean login;
    private String phone;

    public UserProfileDto(User user) {
        id = user.getId();
        name = user.getName();
        username = user.getUsername();
        imageUrl = user.getImageUrl();
        description = user.getDescription();
        link = user.getLink();
        email = user.getEmail();
        follow = false;
        followerCount = user.getFollowersCount();
        followingCount = user.getFollowingsCount();
        posts = user.getPosts();
        postCount = user.getPostsCount();
        login = false;
        phone = user.getPhone();
    }
}
