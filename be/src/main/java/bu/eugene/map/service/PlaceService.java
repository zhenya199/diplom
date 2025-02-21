package bu.eugene.map.service;

import bu.eugene.map.dto.PlaceDto;
import bu.eugene.map.exception.PlaceNotFoundException;
import bu.eugene.map.model.Place;
import bu.eugene.map.repository.PlaceRepository;
import bu.eugene.map.service.api.GeoapifyApiService;
import bu.eugene.map.util.Dto2EntityConverter;
import bu.eugene.map.util.Entity2DtoConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

        private final GeoapifyApiService geoapifyApiService;
        private final PlaceRepository placeRepository;
        private final Dto2EntityConverter dto2EntityConverter;
        private final Entity2DtoConverter entity2DtoConverter;
        private final String BELARUS_COUNTRY_CODE = "by";

        public Place findById(Integer id) {
            return placeRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("ошибка поиска места")
            );
        }

        public Place findByPlaceId(String placeId) {
            return placeRepository.findById(Integer.valueOf(
                    placeId.replace("[object Object],", "")))
                    .orElseThrow(
                    () -> new EntityNotFoundException("ошибка поиска места")
            );
        }

        public PlaceDto findOrCreate(PlaceDto placeDto) {
            Optional<Place> place = placeRepository.findByPlaceId(placeDto.getPlaceId());

           if(place.isPresent()) {
               return entity2DtoConverter.convertPlaceEntity2Dto(place.get());
           }
           else {
               return entity2DtoConverter.convertPlaceEntity2Dto(
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
                    placeDtos.add(placeDto);
                }
                return placeDtos;
            }
            throw new PlaceNotFoundException("Упс. Похоже такого места не существует.");
        }

        private void createPlaceDtoFromJson(JSONArray results, int i, PlaceDto placeDto) {
            JSONObject result = results.getJSONObject(i);

            if(result.has("country_code") &&
                result.getString("country_code")
                        .equals(BELARUS_COUNTRY_CODE)) {
            placeDto.setName(result.getString("city"));
            placeDto.setTypeOfPlace(result.getString("result_type"));
            placeDto.setLat(result.getDouble("lat"));
            placeDto.setLon(result.getDouble("lon"));
            placeDto.setCountry(result.getString("country"));
            placeDto.setPlaceId(result.getString("place_id"));
            if (result.has("suburb")) {
                placeDto.setSuburb(result.getString("suburb"));
            }
        }
     }
}
