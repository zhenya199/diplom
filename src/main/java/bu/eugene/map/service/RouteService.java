package bu.eugene.map.service;

import bu.eugene.map.dto.RouteDto;
import bu.eugene.map.model.Person;
import bu.eugene.map.model.Place;
import bu.eugene.map.model.RouteEntity;
import bu.eugene.map.repository.RouteRepository;
import bu.eugene.map.util.Dto2EntityConverter;
import bu.eugene.map.util.Entity2DtoConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final Dto2EntityConverter dto2EntityConverter;
    private final Entity2DtoConverter entity2DtoConverter;
    private final PlaceService placeService;
    private final PersonService personService;


    @Transactional
    public void crateRoute(RouteDto routeDto, String token) {
        Person author = personService.getPersonFromToken(token);
        Place place =  placeService.findByPlaceId(routeDto.getPlaceId());
        RouteEntity entity = dto2EntityConverter.convertRouteDto2RouteEntity(routeDto);
        entity.setAuthor(author);
        entity.setPlace(place);
        routeRepository.save(entity);
    }

    public Page<RouteDto> findAllByPerson(String token, Pageable pageable) {
        Person author = personService.getPersonFromToken(token);
        return routeRepository.findAllByUserId(author.getId(), pageable)
                .map(entity2DtoConverter::convertRouteEntity2Dto);
    }

    public Page<RouteDto> getRoutePage(Pageable pageable) {
        return routeRepository.findAll(pageable)
                .map(entity2DtoConverter::convertRouteEntity2Dto);
    }
}
