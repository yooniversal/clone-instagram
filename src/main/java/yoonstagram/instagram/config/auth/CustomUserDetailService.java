package yoonstagram.instagram.config.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        // 아이디 체크
        List<User> users = userRepository.findByName(name);

        // 이메일 체크
        if (users.size() == 0) users = userRepository.findByEmail(name);

        if (users.size() > 0) {
            // DB에 정보가 존재하면 USER라는 권한 제공
            grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
            return new PrincipalDetails(users.get(0));
        } else {
            // DB에 정보가 존재하지 않으므로 exception 호출
            throw new UsernameNotFoundException("cannot find User by name : " + name);
        }
    }

}