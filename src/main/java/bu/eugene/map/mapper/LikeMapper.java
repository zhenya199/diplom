package bu.eugene.map.mapper;

import bu.eugene.map.dto.LikeDto;
import bu.eugene.map.model.LikeEntity;
import bu.eugene.map.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(source = "author", target = "author", qualifiedByName = "personToString")
    @Mapping(source = "image.id", target = "imageId")
    LikeDto like2Dto(LikeEntity likeEntity);

    List<LikeDto> likeListToDtoList(List<LikeEntity> likes);

    @Named("personToString")
    default String personToString(Person person) {
        return person != null ? person.getUsername() : null;
    }
}
