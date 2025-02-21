package bu.eugene.map.controller;

import bu.eugene.map.dto.PersonDto;
import bu.eugene.map.service.AuthService;
import bu.eugene.map.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity registration(@ModelAttribute PersonDto personDto) {
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

