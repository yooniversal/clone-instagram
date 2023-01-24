package yoonstagram.instagram.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Oauth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);    //유저 정보 가져옴
        Map<String, Object> user_map = oAuth2User.getAttributes();
        String email = (String) user_map.get("email");
        String name = (String) user_map.get("name");
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()); //랜덤한 비밀번호 생성

        List<User> check_users = userRepository.findByEmail(email);
        if (check_users.size() == 0) {
            //최초 로그인
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(null);
            user.setName(name);
            userRepository.save(user);

            //oauth2로 로그인됬는지 구분할 수 있음
            return new PrincipalDetails(user, user_map);
        } else {
            //이미 회원가입 되어있음
            User check_user = check_users.get(0);
            return new PrincipalDetails(check_user);
        }
    }

}
