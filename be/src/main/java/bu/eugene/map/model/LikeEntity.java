package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "like_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @CreationTimestamp
        private LocalDateTime createdAt;

        private int rate;

        @ManyToOne
        @JoinColumn(name = "person_id")
        private Person author;

        @ManyToOne
        @JoinColumn(name = "image_id")
        private ImageEntity image;
}