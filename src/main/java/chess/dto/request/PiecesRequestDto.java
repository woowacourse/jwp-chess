package chess.dto.request;

public class PiecesRequestDto {

    private int roomId;

    public PiecesRequestDto() {
    }

    public PiecesRequestDto(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}
