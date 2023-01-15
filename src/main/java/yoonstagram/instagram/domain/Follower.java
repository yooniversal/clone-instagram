package yoonstagram.instagram.domain;

import javax.persistence.*;

@Entity
public class Follower {

    @Id @GeneratedValue
    @Column(name = "follower_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
