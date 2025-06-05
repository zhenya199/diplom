package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class ImageEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "person_id")
        private Person person;

        private String pathToFile;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @Length(max = 2000, message = "описание не может быть больше 2000 символов")
        private String description;

        @ManyToOne
        @JoinColumn(name = "place_id")
        private Place place;

        @ManyToOne
        @JoinColumn(name = "route_id")
        private RouteEntity route;

        @OneToMany(mappedBy = "image", cascade = CascadeType.REMOVE)
        private List<LikeEntity> likes = new ArrayList<>();

        @OneToMany(mappedBy = "image", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
        private List<CommentEntity> comments = new ArrayList<>();

        public void addLikeEntity(LikeEntity likeEntity) {
                this.likes.add(likeEntity);
        }

        public void removeLikeEntity(LikeEntity likeEntity) {
                this.likes.remove(likeEntity);
        }
}