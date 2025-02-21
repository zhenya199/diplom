package bu.eugene.map.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CommentDto {

        private Integer id;
        private String usernameAuthor;
        private String body;
        private Integer imageId;
        private LocalDateTime createdAt;
}
