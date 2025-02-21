package bu.eugene.map.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String role;
    private String username;
    private String password;
    private String pathToProfileImage;
    private MultipartFile profileImage;
    private List<ImageDto> images = new ArrayList<>();
}
