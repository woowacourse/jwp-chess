package chess.dao.dto;

import chess.domain.position.Position;

public class ChessPieceDeleteDto {

    private final int roomId;
    private final String position;

    public ChessPieceDeleteDto(int roomId, Position position) {
        this.roomId = roomId;
        this.position = position.getValue();
    }

    public int getRoomId() {
        return roomId;
    }

    public String getPosition() {
        return position;
    }
}
