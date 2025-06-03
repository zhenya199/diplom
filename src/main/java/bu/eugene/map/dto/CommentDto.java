package bu.eugene.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

        private Integer id;
        private String author;
        private String authorImage;
        private String value;
        private Integer image;
        private Integer route;
        private LocalDateTime createdAt;
}
