package bu.eugene.map.service;

import bu.eugene.map.dto.CommentDto;
import bu.eugene.map.exception.CannotDeleteCommentException;
import bu.eugene.map.model.CommentEntity;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.CommentRepository;
import bu.eugene.map.util.Dto2EntityConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

        private final CommentRepository commentRepository;
        private final Dto2EntityConverter dto2EntityConverter;
        private final ImageService imageService;
        private final PersonService personService;

        @Transactional
        public void addComment(CommentDto commentDto) {
            CommentEntity comment = dto2EntityConverter.convertCommentDto2CommentEntity(commentDto);
            comment.setImage(imageService.getImageById(commentDto.getImage()));
            comment.setAuthor(personService.findByUsername(commentDto.getAuthor()));
            commentRepository.save(comment);
        }

        public void deleteComment(Integer id, String token) {
            Person currentUser = personService.getPersonFromToken(token);
            Optional<CommentEntity> comment = commentRepository.findById(id);
            if(comment.isPresent()) {
                if (comment.get().getAuthor().equals(currentUser)) {
                    commentRepository.delete(comment.get());
                } else {
                   throw new CannotDeleteCommentException("Вы не можете удалить коментарий");
                }
            }
        }
}
