package bu.eugene.map.service;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.dto.RouteDto;
import bu.eugene.map.exception.CannotDeleteRouteException;
import bu.eugene.map.exception.RouteNotFoundException;
import bu.eugene.map.model.ImageEntity;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final Dto2EntityConverter dto2EntityConverter;
    private final Entity2DtoConverter entity2DtoConverter;
    private final PlaceService placeService;
    private final PersonService personService;
    private final ImageService imageService;


    @Transactional
    public void crateRoute(RouteDto routeDto, String token) {
        Person author = personService.getPersonFromToken(token);
        Place place =  placeService.findByPlaceId(routeDto.getPlaceId());
        RouteEntity entity = dto2EntityConverter.convertRouteDto2RouteEntity(routeDto);
        entity.setAuthor(author);
        entity.setPlace(place);
        routeRepository.save(entity);
    }

    public RouteDto addImage(Integer routeId, ImageDto imageDto) {
        RouteEntity route = getRouteById(routeId);
        ImageEntity saved = imageService.saveImageWithoutPlace(imageDto, route);
        route.addImage(saved.getPathToFile());
        route.addImageEntity(saved);
        RouteEntity newRoute = routeRepository.save(route);
        return entity2DtoConverter.convertRouteEntity2Dto(newRoute);
    }

    public RouteDto findRouteDtoById(Integer routeId) {
        RouteEntity route = getRouteById(routeId);
        return entity2DtoConverter.convertRouteEntity2Dto(route);
    }

    public void deleteRoute(String token, Integer id) {
        Person currentUser = personService.getPersonFromToken(token);
        RouteEntity routeEntity = getRouteById(id);

        if (routeEntity.getAuthor().equals(currentUser) ||
                currentUser.getRole().equals("ADMIN")) {
           routeRepository.delete(routeEntity);
        } else {
            throw new CannotDeleteRouteException("Ошибка удаления маршрута");
        }

    }

    public RouteEntity getRouteById(Integer routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException("Маршрут не найден"));
    }

    public List<RouteDto> findAllByPerson(String token, Pageable pageable) {
        Person author = personService.getPersonFromToken(token);
        return routeRepository.findAllByUserId(author.getId(), pageable).stream()
                .map(entity2DtoConverter::convertRouteEntity2Dto)
                .toList();
    }

    public Page<RouteDto> getRoutePage(Pageable pageable) {
        return routeRepository.findAll(pageable)
                .map(entity2DtoConverter::convertRouteEntity2Dto);
    }
}