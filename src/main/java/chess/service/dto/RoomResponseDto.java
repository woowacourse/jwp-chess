package chess.service.dto;

public class RoomResponseDto {

    private final int id;
    private final String title;

    public RoomResponseDto(final int id, final String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
