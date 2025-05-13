package bu.eugene.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteDto {

        private Integer id;
        private Integer authorId;
        private String name;
        private String description;
        private String placeName;
        private String placeId;
}
