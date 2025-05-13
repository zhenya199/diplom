package bu.eugene.map.mapper;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.LikeEntity;
import bu.eugene.map.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {LikeMapper.class, CommentMapper.class, PlaceMapper.class})
public interface ImageMapper {

    @Mapping(source = "person.username", target = "author")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "place", target = "place")
    ImageDto image2Dto(ImageEntity image);

    List<ImageDto> imageListToDtoList(List<ImageEntity> images);

    @Named("personToString")
    default String personToString(Person person) {
        return person != null ? person.getUsername() : null;
    }
}
