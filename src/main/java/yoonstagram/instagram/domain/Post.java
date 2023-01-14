package yoonstagram.instagram.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private int likeCount;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
