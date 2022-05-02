package chess.dto;

public class RoomResponse {

    private Long id;
    private String title;

    public RoomResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
