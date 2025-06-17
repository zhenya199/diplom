package bu.eugene.map.controller;

import bu.eugene.map.dto.PersonDto;
import bu.eugene.map.service.AuthService;
import bu.eugene.map.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class AuthController {

    private final AuthService authService;

    @RequestMapping(method = RequestMethod.OPTIONS, value = "/auth")
    public ResponseEntity<Void> handleAccountOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, OPTIONS")
                .header("Access-Control-Allow-Headers", "Authorization")
                .build();
    }

    @PostMapping(value = "/registration", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registration(
            @RequestPart("username") String username,
            @RequestPart("firstName") String firstName,
            @RequestPart("lastName") String lastName,
            @RequestPart("password") String password,
            @RequestPart(value = "email", required = false) String email,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        PersonDto personDto = PersonDto.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .email(email)
                .profileImage(profileImage)
                .build();

        return authService.registration(personDto);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody PersonDto personDto) {
        return authService.login(personDto);
    }

    @PostMapping("/refresh")
    public Map<String, String> refreshToken(@RequestHeader("Authorization") String token) {
        return authService.refreshToken(token);
    }
}

