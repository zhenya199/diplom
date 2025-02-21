package bu.eugene.map.controller;

import bu.eugene.map.dto.PersonDto;
import bu.eugene.map.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class PersonController {

        private final PersonService personService;

        @GetMapping("/account")
        public ResponseEntity<PersonDto> getPersonData(@RequestHeader("Authorization") String token) {
                try {
                        PersonDto personDto = personService.getPersonDtoFromToken(token);
                        return ResponseEntity.ok(personDto);
                } catch (AuthenticationException e) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
        }
}