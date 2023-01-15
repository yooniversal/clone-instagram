package yoonstagram.instagram.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Image {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String fileName;
    private String fileOriName;
    private String url;
}
