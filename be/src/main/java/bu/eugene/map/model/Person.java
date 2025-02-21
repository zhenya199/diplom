package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Person {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column(unique = true)
        @Length(min = 3, max = 20, message = "юзернейм должен быть не меньше 3 и не больше 20")
        private String username;

        private String password;

        private String firstName;

        private String lastName;

        private String role;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        private String pathToProfileImage;

        @OneToMany(mappedBy = "person")
        private List<ImageEntity> images = new ArrayList<>();

        @OneToMany(mappedBy = "author")
        private List<CommentEntity> comments = new ArrayList<>();

        @OneToMany(mappedBy = "author")
        private List<LikeEntity> likes = new ArrayList<>();
}