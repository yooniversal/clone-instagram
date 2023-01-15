package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(User user) {
        // ID 중복 검증
        validateDuplicateUser(user);

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        List<User> findUsers = userRepository.findByName(user.getName());
        if(!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<User> findMembers() {
        return userRepository.findAll();
    }

    public User findOneById(Long id) {
        return userRepository.findOneById(id);
    }

    public User findByName(String name) {
        List<User> findUsers = userRepository.findByName(name);
        return findUsers.get(0);
    }

    @Transactional
    public void update(Long id, String username) {
        User user = userRepository.findOneById(id);
        user.setUsername(username);
    }
}
