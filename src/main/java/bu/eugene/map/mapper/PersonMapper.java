package bu.eugene.map.mapper;

import bu.eugene.map.dto.PersonDto;
import bu.eugene.map.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RouteMapper.class, ImageMapper.class})
public interface PersonMapper {

    @Mapping(source = "routes", target = "routes")
    @Mapping(source = "images", target = "images")
    PersonDto person2Dto(Person person);

    List<PersonDto> personListToDtoList(List<Person> persons);
}
