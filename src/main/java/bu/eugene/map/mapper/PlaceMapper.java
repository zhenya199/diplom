package bu.eugene.map.mapper;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface PlaceMapper {

    @Mapping(source = "images", target = "images", qualifiedByName = "convertImages")
    PlaceDto place2Dto(Place place);

    List<PlaceDto> placeListToDtoList(List<Place> places);

    @Named("convertImages")
    default List<String> convertImages(List<ImageEntity> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        return images.stream()
                .map(ImageEntity::getPathToFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}