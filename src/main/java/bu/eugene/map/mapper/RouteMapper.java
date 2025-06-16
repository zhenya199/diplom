package bu.eugene.map.mapper;

import bu.eugene.map.dto.RouteDto;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.model.RouteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PlaceMapper.class})
public interface RouteMapper {

    @Mapping(source = "place.name", target = "placeName")
    @Mapping(source = "place.id", target = "placeId")
    @Mapping(source = "author.id", target = "authorId")
    RouteDto route2Dto(RouteEntity route);

    List<RouteDto> routeListToDtoList(List<RouteEntity> routes);

    default Integer mapRouteToId(RouteEntity route) {
        return route != null ? route.getId() : null;
    }

    default Integer mapImageToId(ImageEntity image) {
        return image != null ? image.getId() : null;
    }

    default Integer mapPersonToId(Person person) {
        return person != null ? person.getId() : null;
    }
}
