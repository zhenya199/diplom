package bu.eugene.map.service;

import bu.eugene.map.dto.PersonDto;
import bu.eugene.map.exception.PasswordDoesntMatchException;
import bu.eugene.map.exception.UsernameIsTakenException;
import bu.eugene.map.jwt.JwtUtil;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.PersonRepository;
import bu.eugene.map.util.Dto2EntityConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonRepository personRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final Dto2EntityConverter converter;
    private final ImageService imageService;

    @Transactional
    public ResponseEntity registration(PersonDto dto) {
        processRegistration(dto);
        Person person = converter.convertPersonDto2PersonEntity(dto);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("USER");
        person.setPathToProfileImage(imageService
                .saveImagesLocal(dto.getProfileImage()));
        personRepository.save(person);
        return new ResponseEntity(HttpStatus.OK);
    }


    public Map<String, String> refreshToken(String token) {
        String username = jwtUtil.validateTokenAndRetrieveClaim(token);
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("cannot find person"));
        String role = person.getRole();
        return jwtUtil.refreshTokens(token, role);
    }

    public Map<String, String> login(PersonDto personDto) {
        Map<String, String> tokens = new HashMap<>();
        Person person = personRepository.findByUsername(personDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("неверный юзернейм"));
        if(passwordEncoder.matches(personDto.getPassword(), person.getPassword())) {
            tokens.put("access_token", jwtUtil.generateAccessToken(person.getUsername(), person.getRole()));
            tokens.put("refresh_token", jwtUtil.generateRefreshToken(person.getUsername()));
            return tokens;
        }
        throw new PasswordDoesntMatchException("пароль неверный");
    }

    private void processRegistration(PersonDto dto) {
        Optional<Person> foundedPerson = personRepository.findByUsername(dto.getUsername());
        if (foundedPerson.isPresent()) {
            throw new UsernameIsTakenException("юзернейм занят другим пользователем!");
        }
    }


}
