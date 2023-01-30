package yoonstagram.instagram.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Notification {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private Long postId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    private String imageUrl;
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    public Notification(User user, User fromUser, String imageUrl, NotificationStatus status, Long postId) {
        this.user = user;
        this.fromUser = fromUser;
        this.imageUrl = imageUrl;
        time = LocalDateTime.now();
        this.status = status;
        this.postId = postId;
    }

}
