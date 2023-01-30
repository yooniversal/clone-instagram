package yoonstagram.instagram.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yoonstagram.instagram.config.auth.PrincipalDetails;
import yoonstagram.instagram.domain.Notification;
import yoonstagram.instagram.domain.User;
import yoonstagram.instagram.domain.dto.NotificationDto;
import yoonstagram.instagram.domain.dto.SimpleUserDto;
import yoonstagram.instagram.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/user/notification/")
    public ResponseEntity<?> followingOfUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User currentUser = principalDetails.getUser();
        List<Notification> notifications = notificationService.notificationsWithUser(currentUser.getId());
        List<NotificationDto> notificationDtos = new ArrayList<>();
        for(Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto(notification);
            notificationDtos.add(notificationDto);
        }

        return new ResponseEntity<>(notificationDtos, HttpStatus.OK);
    }
}
