package chess.service.dto;

public class RoomResponseDto {

    private final String title;

    public RoomResponseDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
