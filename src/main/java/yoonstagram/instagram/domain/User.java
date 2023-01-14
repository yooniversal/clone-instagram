package yoonstagram.instagram.domain;

import jakarta.persistence.*;
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
    private String description;
    private String link;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Follower> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Following> followings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;
}
