package bu.eugene.map.mapper;

import bu.eugene.map.dto.CommentDto;
import bu.eugene.map.model.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "author.username", target = "author")
    @Mapping(source = "author.pathToProfileImage", target = "authorImage")
    @Mapping(source = "text", target = "value")
    @Mapping(source = "image.id", target = "image")
    CommentDto comment2Dto(CommentEntity comment);

    List<CommentDto> commentListToDtoList(List<CommentEntity> comments);
}
