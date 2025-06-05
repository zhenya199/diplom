package bu.eugene.map.service;

import bu.eugene.map.dto.PersonDto;
import bu.eugene.map.jwt.JwtUtil;
import bu.eugene.map.mapper.PersonMapper;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.PersonRepository;
import bu.eugene.map.util.Entity2DtoConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

        private final PersonRepository personRepository;
        private final JwtUtil jwtUtil;
        private final PersonMapper personMapper;

        public Person findByUsername(String username) {
            return personRepository.findByUsername(username)
                    .orElseThrow(
                            () -> new EntityNotFoundException("пользователь не найден")
                    );
        }

        public Person getPersonFromToken(String token) {
            return findByUsername(
                    jwtUtil.validateTokenAndRetrieveClaim(token.substring(7))
            );
        }

        public PersonDto getPersonDtoFromToken(String token) {
            return personMapper.person2Dto(getPersonFromToken(token));
        }
}
