package bu.eugene.map.util;

import bu.eugene.map.dto.*;
import bu.eugene.map.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@RequiredArgsConstructor
public class Entity2DtoConverter {

    private final ModelMapper modelMapper;

    public PlaceDto convertPlaceEntity2Dto(Place place) {
        PlaceDto placeDto = modelMapper.map(place, PlaceDto.class);
        return placeDto;
    }

    public ImageDto convertImageEntity2Dto(ImageEntity image) {
       ImageDto imageDto = modelMapper.map(image, ImageDto.class);
       imageDto.setAuthorUsername(image.getPerson().getUsername());
       imageDto.setDescription(image.getDescription());
       for(LikeEntity like : image.getLikeEntity()) {
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
                .authorUsername(like.getAuthor().getUsername())
                .imageId(like.getImage().getId())
                .build();
    }

    public CommentDto convertCommentEntity2Dto(CommentEntity comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .body(comment.getText())
                .createdAt(comment.getCreatedAt())
                .usernameAuthor(comment.getAuthor().getUsername())
                .imageId(comment.getImage().getId())
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