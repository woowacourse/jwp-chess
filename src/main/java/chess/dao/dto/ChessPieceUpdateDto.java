package chess.dao.dto;

import chess.domain.position.Position;

public class ChessPieceUpdateDto {

    private final int roomId;
    private final String from;
    private final String to;

    public ChessPieceUpdateDto(int roomId, Position from, Position to) {
        this.roomId = roomId;
        this.from = from.getValue();
        this.to = to.getValue();
    }

    public int getRoomId() {
        return roomId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
