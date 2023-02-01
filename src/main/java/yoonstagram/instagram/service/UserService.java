package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonstagram.instagram.controller.UserForm;
import yoonstagram.instagram.domain.Post;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.repository.UserRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    public boolean isNameExist(String name) {
        List<User> findUsers = userRepository.findByName(name);
        if(!findUsers.isEmpty()) return true;
        return false;
    }

    public boolean isEmailExist(String email) {
        List<User> findUsers = userRepository.findByEmail(email);
        if(!findUsers.isEmpty()) return true;
        return false;
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public User findOneById(Long id) {
        return userRepository.findOneById(id);
    }

    public User findByName(String name) {
        List<User> findUsers = userRepository.findByName(name);
        return findUsers.get(0);
    }

    public List<User> findBySimilarName(String name) {
        List<User> findUsers = userRepository.findBySimilarName(name);
        return findUsers;
    }

    @Transactional
    public void update(Long id, String username) {
        User user = userRepository.findOneById(id);
        user.setUsername(username);
    }

    @Value("${profileImage.path}")
    private String uploadProfileFolder;

    @Transactional
    public void updateProfile(UserForm form, MultipartFile file) {
        User user = userRepository.findOneById(form.getId());

        if(file != null && !file.isEmpty()) { //파일이 업로드 되었는지 확인
            String imageFileName = user.getId() + "_" + file.getOriginalFilename();
            Path imageFilePath = Paths.get(uploadProfileFolder + imageFileName);
            try {
                if (!user.getImageUrl().equals("null.jpg")) { // 이미 프로필 사진이 있을경우
                    File currentfile = new File(uploadProfileFolder + user.getImageUrl());
                    currentfile.delete(); // 원래파일 삭제
                }
                Files.write(imageFilePath, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setImageUrl(imageFileName);
        }

        user.setEmail(form.getEmail());
        user.setUsername(form.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(form.getPassword()));
        user.setLink(form.getLink());
        user.setDescription(form.getDescription());
        user.setPhone(form.getPhone());
    }

}
