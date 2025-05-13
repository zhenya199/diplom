package bu.eugene.map.util;

import bu.eugene.map.dto.*;
import bu.eugene.map.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Configuration
@RequiredArgsConstructor
public class Entity2DtoConverter {

    private final ModelMapper modelMapper;

    public PlaceDto convertPlaceEntity2Dto(Place place) {
        PlaceDto placeDto = modelMapper.map(place, PlaceDto.class);
       placeDto.setImages(place.getImages().stream()
                .map(ImageEntity::getPathToFile).toList()
       );
        return placeDto;
    }

    public RouteDto convertRouteEntity2Dto(RouteEntity route) {
        return RouteDto.builder()
                .description(route.getDescription())
                .name(route.getName())
                .placeId(route.getPlace().getPlaceId())
                .placeName(route.getPlace().getName())
                .id(route.getId())
                .build();
    }

    public ImageDto convertImageEntity2Dto(ImageEntity image) {
       ImageDto imageDto = new ImageDto();
       imageDto.setId(image.getId());
       imageDto.setPlace(
               convertPlaceEntity2Dto(image.getPlace())
       );
       imageDto.setPathToFile(image.getPathToFile());
       imageDto.setAuthor(image.getPerson().getUsername());
       imageDto.setDescription(image.getDescription());
       for(LikeEntity like : image.getLikes()) {
           imageDto.addLike(convertLikeEntity2Dto(like));
       }
       for(CommentEntity comment : image.getComments()) {
           imageDto.addComment(convertCommentEntity2Dto(comment));
       }
       imageDto.setPathToFile(image.getPathToFile());
       return imageDto;
    }

    public LikeDto convertLikeEntity2Dto(LikeEntity like) {
        return LikeDto.builder()
                .id(like.getId())
                .author(like.getAuthor().getUsername())
                .imageId(like.getImage().getId())
                .build();
    }

    public CommentDto convertCommentEntity2Dto(CommentEntity comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .value(comment.getText())
                .createdAt(comment.getCreatedAt())
                .authorImage(comment.getAuthor().getPathToProfileImage())
                .author(comment.getAuthor().getUsername())
                .image(comment.getImage().getId())
                .build();
    }

    public PersonDto convertPersonToDto(Person person) {
        return  PersonDto.builder()
                .id(person.getId())
                .username(person.getUsername())
                .pathToProfileImage(person.getPathToProfileImage())
                .images(person.getImages().stream().map(this::convertImageEntity2Dto).toList())
                .build();
    }
}