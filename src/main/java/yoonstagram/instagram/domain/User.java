package yoonstagram.instagram.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class User {

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
    private List<Notification> notifications;

    public User() {
        this.username = "";
        this.description = "";
        this.link = "";
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
