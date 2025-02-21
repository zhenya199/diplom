package bu.eugene.map.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "place")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Place {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String name;

        private Double lat;

        private Double lon;

        private String country;

        private String placeId;

        @Column(name = "type")
        private String typeOfPlace;

        private String suburb;

        @Column(name = "rate")
        private Double placeRate;

        @OneToMany(mappedBy = "place")
        private List<ImageEntity> images = new ArrayList<>();

        @OneToMany(mappedBy = "place")
        private List<TouristMap> touristMaps = new ArrayList<>();
}