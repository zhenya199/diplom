package bu.eugene.map.util;

import bu.eugene.map.dto.*;
import bu.eugene.map.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Dto2EntityConverter {

    private final ModelMapper modelMapper;

    public Person convertPersonDto2PersonEntity(PersonDto personDto) {
        Person person = new Person();
        modelMapper.map(personDto, person);
        return person;
    }

    public CommentEntity convertCommentDto2CommentEntity(CommentDto commentDto) {
        CommentEntity commentEntity = new CommentEntity();
        modelMapper.map(commentDto, commentEntity);
        commentEntity.setText(commentDto.getValue());
        return commentEntity;
    }

    public RouteEntity convertRouteDto2RouteEntity(RouteDto routeDto) {
        RouteEntity routeEntity = new RouteEntity();
        routeEntity.setDescription(routeDto.getDescription());
        routeEntity.setName(routeDto.getName());
        return routeEntity;
    }

    public Place convertPlaceDto2PlaceEntity(PlaceDto placeDto) {
        Place place = new Place();
        modelMapper.map(placeDto, place);
        place.setName(placeDto.getSuburb());
        return place;
    }

    public ImageEntity convertImageDto2ImageEntity(ImageDto imageDto) {
        ImageEntity imageEntity = new ImageEntity();
        modelMapper.map(imageDto, imageEntity);
        return imageEntity;
    }
}
