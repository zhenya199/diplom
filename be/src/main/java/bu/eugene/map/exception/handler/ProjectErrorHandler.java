package bu.eugene.map.exception.handler;

import bu.eugene.map.exception.CannotDeleteLikeException;
import bu.eugene.map.exception.PasswordDoesntMatchException;
import bu.eugene.map.exception.PlaceNotFoundException;
import bu.eugene.map.exception.UsernameIsTakenException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class ProjectErrorHandler {

        @ExceptionHandler(UsernameIsTakenException.class)
        public ResponseEntity<?> usernameIsTakenHandler(UsernameIsTakenException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(PasswordDoesntMatchException.class)
        public ResponseEntity<?> passwordDoesntMatchExceptionHandler(PasswordDoesntMatchException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(PlaceNotFoundException.class)
        public ResponseEntity<?> placeNotFoundExceptionHandler(PlaceNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(JWTVerificationException.class)
        public ResponseEntity<?> jWTVerificationExceptionHandler(JWTVerificationException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<?> accessDeniedExceptionHandler(AccessDeniedException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(CannotDeleteLikeException.class)
        public ResponseEntity<?> cannotDeleteLikeExceptionHandler(CannotDeleteLikeException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseEntity<?> cannotDeleteLikeExceptionHandler(MaxUploadSizeExceededException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>("фотография весит слишком много! Максимальный размер 5мб", HttpStatus.BAD_REQUEST);
        }
}
