package chess.dto;

public class PiecesDto {
    private final int room_id;
    private final String piece_name;
    private final String position;

    public PiecesDto(int room_id, String piece_name, String position) {
        this.room_id = room_id;
        this.piece_name = piece_name;
        this.position = position;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getPiece_name() {
        return piece_name;
    }

    public String getPosition() {
        return position;
    }
}
