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
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonController {

        private final PersonService personService;

        @RequestMapping(method = RequestMethod.OPTIONS, value = "/account")
        public ResponseEntity<Void> handleAccountOptions() {
                return ResponseEntity.ok()
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                        .header("Access-Control-Allow-Headers", "Authorization")
                        .build();
        }

        @GetMapping("/account")
        public PersonDto getPersonData(@RequestHeader("Authorization") String token) {
                try {
                        return personService.getPersonDtoFromToken(token);
                } catch (AuthenticationException e) {
                        return null;
                }
        }
}