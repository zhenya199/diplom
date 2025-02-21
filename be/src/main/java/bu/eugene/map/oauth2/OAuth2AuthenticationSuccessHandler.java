package bu.eugene.map.oauth2;

import bu.eugene.map.jwt.JwtUtil;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String email = oidcUser.getAttribute("email");
        Person person = personRepository.findByUsername(email).orElseThrow(
                () -> new EntityNotFoundException("ошибка авторизации")
        );

        if(person == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String accessToken = jwtUtil.generateAccessToken(person.getUsername(), person.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(person.getUsername());
        Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access-token", accessToken);
        responseBody.put("refresh-token", refreshToken);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), responseBody);

    }
}