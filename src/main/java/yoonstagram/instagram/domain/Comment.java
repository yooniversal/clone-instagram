package yoonstagram.instagram.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    public Comment(User user, Post post, String content) {
        this.userId = user.getId();
        this.post = post;
        this.content = content;
    }
}
