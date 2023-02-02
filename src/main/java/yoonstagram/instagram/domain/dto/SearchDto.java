package yoonstagram.instagram.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchDto {
    private List<SimpleUserDto> userDtos;
    private Long count;

    public SearchDto(List<SimpleUserDto> userDtos, Long count) {
        this.userDtos = userDtos;
        this.count = count;
    }
}
