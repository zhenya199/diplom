package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

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
        private List<CommentEntity> comments = new ArrayList<>();

        public void addImage(String path) {
                pathsToImages.add(path);
        }
}
