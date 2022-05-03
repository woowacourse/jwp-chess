package chess.dto;

public class RoomDto {
    private final Long boardId;
    private final String title;

    public RoomDto(Long boardId, String title) {
        this.boardId = boardId;
        this.title = title;
    }

    public Long getBoardId() {
        return boardId;
    }

    public String getTitle() {
        return title;
    }
}
