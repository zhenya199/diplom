package bu.eugene.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikeDto {

        private Integer id;
        private String authorUsername;
        private Integer imageId;
}
