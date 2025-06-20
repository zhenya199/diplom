package bu.eugene.map.service;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.exception.PlaceNotFoundException;
import bu.eugene.map.mapper.PlaceMapper;
import bu.eugene.map.model.Place;
import bu.eugene.map.repository.PlaceRepository;
import bu.eugene.map.service.api.GeoapifyApiService;
import bu.eugene.map.util.Dto2EntityConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

        private final GeoapifyApiService geoapifyApiService;
        private final PlaceRepository placeRepository;
        private final Dto2EntityConverter dto2EntityConverter;
        private final PlaceMapper placeMapper;
        private final String BELARUS_COUNTRY_CODE = "by";

        public Place findById(String placeId) {
            return placeRepository.findByPlaceId(
                            placeId.replace("[object Object],", ""))
                    .orElseThrow(
                            () -> new EntityNotFoundException("ошибка поиска места")
                    );
        }

        public Place findByPlaceId(String placeId) {
            return placeRepository.findByPlaceId(placeId)
                    .orElseThrow(
                    () -> new EntityNotFoundException("ошибка поиска места")
            );
        }

        public PlaceDto findByParam(String param) {
            Place place = placeRepository.findByParamIgnoreCase(param)
                    .orElseThrow(() -> new PlaceNotFoundException("ошибка поиска места"));

            return placeMapper.place2Dto(place);
        }

        @Transactional
        public List<PlaceDto> getAllPlaces() {
            return placeRepository.findAll()
                    .stream()
                    .map(placeMapper::place2Dto)
                    .collect(Collectors.toList());
        }

        @Transactional
        public List<PlaceDto> getAllPlacesWithImages() {
            return placeRepository.findAll()
                    .stream()
                    .filter(place -> !place.getImages().isEmpty())
                    .map(placeMapper::place2Dto)
                    .collect(Collectors.toList());
        }

        public PlaceDto findOrCreate(PlaceDto placeDto) {
            Optional<Place> place = placeRepository.findByPlaceId(placeDto.getPlaceId());

           if(place.isPresent()) {
               return placeMapper.place2Dto(place.get());
           }
           else {
               return placeMapper.place2Dto(
                       placeRepository.save(dto2EntityConverter.convertPlaceDto2PlaceEntity(placeDto))
               );
           }
        }

        public List<PlaceDto> getAllPlacesInfoByAPI(String cityName) {
            String dataAboutCity = geoapifyApiService.getCoordinates(cityName);
            if (dataAboutCity != null) {
                JSONObject jsonObject = new JSONObject(dataAboutCity);
                JSONArray results = jsonObject.getJSONArray("results");

                List<PlaceDto> placeDtos = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    PlaceDto placeDto = new PlaceDto();
                    createPlaceDtoFromJson(results, i, placeDto);
                    if (placeDto.getName() != null) {
                        placeDtos.add(placeDto);
                    }
                }
                return placeDtos;
            }
            throw new PlaceNotFoundException("Упс. Похоже такого места не существует.");
        }

        private PlaceDto createPlaceDtoFromJson(JSONArray results, int i, PlaceDto placeDto) {
            JSONObject result = results.getJSONObject(i);

            if(result.has("country_code") &&
                result.getString("country_code")
                        .equals(BELARUS_COUNTRY_CODE)) {
                if (result.has("name")) {
                    String name = result.getString("name");
                    if (name.matches("[a-zA-Zа-яА-Я]+")) {  // Проверка на буквы (латинские и русские)
                        placeDto.setName(name);
                    } else {
                        placeDto.setName(result.getString("city"));
                    }
                } else {
                    placeDto.setName(result.getString("city"));
                }
            placeDto.setTypeOfPlace(result.getString("result_type"));
            placeDto.setLat(result.getDouble("lat"));
            placeDto.setLon(result.getDouble("lon"));
            placeDto.setCountry(result.getString("country"));
            placeDto.setPlaceId(result.getString("place_id"));
            if (result.has("suburb")) {
                placeDto.setSuburb(result.getString("suburb"));
            }

            if (result.has("street")) {

                String street = result.getString("street");
                if (result.has("housenumber")) {
                    String houseNumber = result.getString("housenumber");
                    street = street + " " + houseNumber;
                    placeDto.setStreet(street);
                }
                placeDto.setStreet(street);
            }


        }
            return placeDto;
     }
}
