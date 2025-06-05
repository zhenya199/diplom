package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tourist_map")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class RouteEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String name;

        @Length(max = 2000, message = "описание не может быть больше 2000 символов")
        private String description;

        @ManyToOne
        @JoinColumn(name = "place_id")
        private Place place;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private Person author;

        @ElementCollection
        private List<String> pathsToImages;

        @OneToMany(mappedBy = "route")
        private List<ImageEntity> images = new ArrayList<>();

        @OneToMany(mappedBy = "route")
        private List<CommentEntity> comments = new ArrayList<>();

        public void addImage(String path) {
                pathsToImages.add(path);
        }

        public void addImageEntity(ImageEntity imageEntity) {
                images.add(imageEntity);
        }
}
