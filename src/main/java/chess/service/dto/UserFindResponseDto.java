package chess.service.dto;

public class UserFindResponseDto {

    private String name;

    public UserFindResponseDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
