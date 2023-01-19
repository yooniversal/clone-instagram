package yoonstagram.instagram.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {

    public User() {
        this.username = "";
        this.description = "";
        this.link = "";
    }

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String username;
    private String password;
    private String description;
    private String link;
    private String email;
    private String phone;
    private String imageUrl = "null.jpg";

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Follower> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Following> followings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    public int getFollowersCount() {
        return followers.size();
    }

    public int getFollowingsCount() {
        return followings.size();
    }

    public int getPostsCount() {
        return posts.size();
    }

    public String getUsername() {
        if(username == null) {
            username = name;
        }
        return username;
    }

}
