package bu.eugene.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteDto {

        private Integer id;
        private Integer authorId;
        private String name;
        private String description;
        private String placeName;
        private String lat;
        private String lon;
        private String placeId;
        private List<String> pathsToImages;
}
