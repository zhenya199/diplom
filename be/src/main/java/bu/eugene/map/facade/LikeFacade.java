package bu.eugene.map.facade;

import bu.eugene.map.dto.LikeDto;
import bu.eugene.map.exception.CannotDeleteLikeException;
import bu.eugene.map.model.LikeEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.LikeRepository;
import bu.eugene.map.service.ImageService;
import bu.eugene.map.service.LikeService;
import bu.eugene.map.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeFacade {

        private final LikeService likeService;
        private final PersonService personService;
        private final ImageService imageService;
        private final LikeRepository likeRepository;

        public LikeDto createLike(String token, Integer imageId, Integer rate) {
            return likeService.createLike(rate,
                    personService.getPersonFromToken(token),
                    imageService.getImageById(imageId));
        }

        public ResponseEntity<?> deleteLike(String token, Integer likeId) {
                return likeService.removeLike(personService.getPersonFromToken(token), likeId);
        }
}
