package chess.dto;

public class PiecesDto {
    private final int roomId;
    private final String pieceName;
    private final String position;

    public PiecesDto(int roomId, String pieceName, String position) {
        this.roomId = roomId;
        this.pieceName = pieceName;
        this.position = position;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPosition() {
        return position;
    }
}
