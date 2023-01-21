package yoonstagram.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoonstagram.instagram.domain.Like;
import yoonstagram.instagram.repository.LikeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public List<Like> findLikesWithUser(Long userId) {
        return likeRepository.findLikesWithUser(userId);
    }
}
