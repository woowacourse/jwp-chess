package chess.dto;

public class RoomRes {

    private Long id;
    private String title;

    public RoomRes(Long id, String title) {
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
