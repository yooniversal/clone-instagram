package yoonstagram.instagram.domain;

import javax.persistence.*;

@Entity
public class Following {

    @Id @GeneratedValue
    @Column(name = "following_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
