package yoonstagram.instagram.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private int likeCount;

    private String imageUrl;
    private String description;
    private LocalDateTime date;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Post createPost(String imageUrl, String description, User user) {
        Post post = new Post();
        post.setImageUrl(imageUrl);
        post.setDescription(description);
        post.setUser(user);
        post.setDate(LocalDateTime.now());
        user.getPosts().add(post);
        return post;
    }
}
