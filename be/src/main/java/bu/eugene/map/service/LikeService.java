package bu.eugene.map.service;

import bu.eugene.map.dto.LikeDto;
import bu.eugene.map.exception.CannotDeleteLikeException;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.LikeEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.LikeRepository;
import bu.eugene.map.util.Entity2DtoConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

        private final LikeRepository likeRepository;
        private final Entity2DtoConverter entity2DtoConverter;

        public LikeEntity findById(Integer id) {
            LikeEntity like = likeRepository.findById(id)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Like not found")
                    );
            return like;
        }

        public LikeDto createLike(Integer rate, Person person, ImageEntity image) {
            LikeEntity like = new LikeEntity();
            like.setRate(rate);
            like.setImage(image);
            like.setAuthor(person);
            return entity2DtoConverter.convertLikeEntity2Dto(likeRepository.save(like));
        }
        public ResponseEntity<?> removeLike(Person author, Integer likeId) {
            canPersonDeleteLike(author, getLikeById(likeId));
            likeRepository.deleteById(likeId);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        private void canPersonDeleteLike(Person author, LikeEntity like) {
            if(!like.getAuthor().getUsername().equals(author.getUsername())) {
                throw new CannotDeleteLikeException(
                        "вы не можете убрать лайк. Вы его даже не ставили!"
                );
            }
        }

        public LikeEntity getLikeById(Integer likeId) {
            return likeRepository.findById(likeId)
                    .orElseThrow(() -> new EntityNotFoundException("ошибка поиска лайка"));
        }
}