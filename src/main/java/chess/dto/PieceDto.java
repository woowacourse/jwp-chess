package chess.dto;

import java.util.Map.Entry;

import chess.domain.piece.Piece;
import chess.domain.position.Position;

public class PieceDto {

    private final int roomId;
    private final String pieceName;
    private final String position;

    public PieceDto(int roomId, String pieceName, String position) {
        this.roomId = roomId;
        this.pieceName = pieceName;
        this.position = position;
    }

    public PieceDto(int roomId, Entry<Position, Piece> entry) {
        this.roomId = roomId;
        this.pieceName = entry.getValue().getName();
        this.position = entry.getKey().chessCoordinate();
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
