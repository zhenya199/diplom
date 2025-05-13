package bu.eugene.map.service;

import bu.eugene.map.dto.ImageDto;
import bu.eugene.map.exception.CannotDeleteLikeException;
import bu.eugene.map.mapper.ImageMapper;
import bu.eugene.map.model.ImageEntity;
import bu.eugene.map.model.LikeEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.LikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

        private final LikeRepository likeRepository;
        private final ImageMapper imageMapper;

        public LikeEntity findById(Integer id) {
            LikeEntity like = likeRepository.findById(id)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Like not found")
                    );
            return like;
        }

        public ImageDto createLike(Person person, ImageEntity image) {
            LikeEntity like = new LikeEntity();
            if(likeRepository.findLikeByImageIdAndPersonId(person.getId(), image.getId()) == null) {
                like.setImage(image);
                like.setAuthor(person);
                LikeEntity savedLike = likeRepository.save(like);
                image.addLikeEntity(savedLike);
                ImageDto dto = imageMapper.image2Dto(image);
                return dto;
            } else {
                throw new IllegalArgumentException("Нельзя создавать лайк еще раз");
            }
        }
        public ImageDto removeLike(Person author, Integer imageId) {
            LikeEntity like = likeRepository.findLikeByImageIdAndPersonId(author.getId(), imageId);
            canPersonDeleteLike(author, getLikeById(like.getId()));
            ImageEntity image = like.getImage();
            likeRepository.delete(like);
            return imageMapper.image2Dto(image);
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