package chess.dto;

public final class RoomDTO {
    private final int id;
    private final String title;
    private final String status;
    private final boolean playing;

    public RoomDTO(final int id, final String title, final String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    private String status(final int status) {
        if (status == 1) {
            return "진행중";
        }
        return "종료됨";
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

    public boolean isPlaying() {
        return playing;
    }
}
