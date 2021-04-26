package chess.dto;

public final class RoomDTO {
    private final int id;
    private final String title;
    private final String status;

    public RoomDTO(final int id, final String title, final String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}
