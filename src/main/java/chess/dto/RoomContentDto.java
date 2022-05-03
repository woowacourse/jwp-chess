package chess.dto;

public class RoomContentDto {

    private final int id;
    private final String title;
    private final int boardId;
    private final String status;

    public RoomContentDto(int id, String title, int boardId, String status) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getBoardId() {
        return boardId;
    }

    public String getStatus() {
        return status;
    }
}
