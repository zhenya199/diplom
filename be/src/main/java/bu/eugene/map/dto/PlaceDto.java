package bu.eugene.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDto {

        private Integer id;
        private String name;
        private Double lat;
        private Double lon;
        private String country;
        private String typeOfPlace;
        private String suburb;
        private String placeId;
}
