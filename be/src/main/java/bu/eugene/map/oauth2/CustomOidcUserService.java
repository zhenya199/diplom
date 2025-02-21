package bu.eugene.map.oauth2;

import bu.eugene.map.jwt.JwtUtil;
import bu.eugene.map.model.Person;
import bu.eugene.map.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

   private final PersonRepository personRepository;
   private final JwtUtil jwtUtil;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes = oidcUser.getAttributes();
        Person userInfo = new Person();
        userInfo.setUsername((String) attributes.get("email"));
        userInfo.setPathToProfileImage((String) attributes.get("picture"));
        userInfo.setFirstName((String) attributes.get("given_name"));
        userInfo.setLastName((String) attributes.get("family_name"));
        updateUser(userInfo);
        return oidcUser;
    }

    private void updateUser(Person userInfo) {
        Person person = personRepository.findByUsername(userInfo.getUsername()).orElse(null);
        if(person == null) {
            person = new Person();
        }
        person.setUsername(userInfo.getUsername());
        person.setPathToProfileImage(userInfo.getPathToProfileImage());
        person.setFirstName(userInfo.getFirstName());
        person.setRole("USER");
        personRepository.save(person);
    }
}
