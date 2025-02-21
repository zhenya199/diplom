package bu.eugene.map.facade;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.service.ImageService;
import bu.eugene.map.service.PersonService;
import bu.eugene.map.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageFacade {

        private final PersonService personService;
        private final ImageService imageService;
        private final PlaceService placeService;

        public ImageDto saveImage(ImageDto imageDto, String placeId, String token) {
            return imageService.saveImage(imageDto,
                    placeService.findByPlaceId(placeId),
                    personService.getPersonFromToken(token));
        }

        public Page<ImageDto> getMyImages(String token, Pageable pageable) {
            Person person = personService.getPersonFromToken(token);
            return imageService.getMyImages(person, pageable);
        }

        public ResponseEntity<?> deletePhoto(String token, Integer imageId) {
            return imageService.deletePhoto(
                    personService.getPersonFromToken(token), imageId);
        }

        public ImageDto updateImage(Integer imageId, String token, String description) {
            return imageService.updatePhoto(imageId,
                    personService.getPersonFromToken(token),
                    description);
        }
}
