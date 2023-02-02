package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.SearchDto;
import yoonstagram.instagram.domain.dto.SimpleUserDto;
import yoonstagram.instagram.service.PostService;
import yoonstagram.instagram.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SearchApiController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/search/{input}")
    public ResponseEntity<?> followingOfUser(@PathVariable("input") String input) {

        List<User> users = userService.findBySimilarName(input);
        List<SimpleUserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            SimpleUserDto userDto = new SimpleUserDto(user, false);
            userDtos.add(userDto);
        }

        Long count = postService.getPostCountWithTag(input);
        SearchDto searchDto = new SearchDto(userDtos, count);

        return new ResponseEntity<>(searchDto, HttpStatus.OK);
    }

}
