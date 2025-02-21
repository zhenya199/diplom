package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

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

        private String description;

        @ManyToOne
        @JoinColumn(name = "place_id")
        private Place place;

        @OneToMany(mappedBy = "image")
        private List<LikeEntity> likeEntity = new ArrayList<>();

        @OneToMany(mappedBy = "image")
        private List<CommentEntity> comments = new ArrayList<>();

        public void addLikeEntity(LikeEntity likeEntity) {
                this.likeEntity.add(likeEntity);
        }

        public void removeLikeEntity(LikeEntity likeEntity) {
                this.likeEntity.remove(likeEntity);
        }
}