package bu.eugene.map.util;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.dto.PersonDto;
import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.model.Place;
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

    public Place convertPlaceDto2PlaceEntity(PlaceDto placeDto) {
        Place place = new Place();
        modelMapper.map(placeDto, place);
        return place;
    }

    public ImageEntity convertImageDto2ImageEntity(ImageDto imageDto) {
        ImageEntity imageEntity = new ImageEntity();
        modelMapper.map(imageDto, imageEntity);
        return imageEntity;
    }
}
