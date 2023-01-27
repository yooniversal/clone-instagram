package yoonstagram.instagram.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Notification {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String imageUrl;
    private LocalDateTime time;
}
