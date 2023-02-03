package yoonstagram.instagram.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Post implements Comparable<Post> {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private Long likeCount;
    private String imageUrl;
    private String description;
    private String tag;
    private LocalDateTime date;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Like> likes = new ArrayList<>();

    public static Post createPost(String imageUrl, String tag, String description, User user) {
        Post post = new Post();
        post.setImageUrl(imageUrl);
        post.setDescription(description);
        post.setTag(tag);
        post.setUser(user);
        post.setDate(LocalDateTime.now());
        user.getPosts().add(post);
        post.setLikeCount(0L);
        return post;
    }

    @Override
    public int compareTo(Post o) {
        return (int) (o.getLikeCount() - getLikeCount());
    }
}
