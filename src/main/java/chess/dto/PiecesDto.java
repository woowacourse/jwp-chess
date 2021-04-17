package chess.dto;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PiecesDto {

    private final int roomId;
    private final List<PieceDto> pieceDtos;

    public PiecesDto(int roomId, Map<Position, Piece> pieces){
        this.roomId = roomId;
        this.pieceDtos = new ArrayList<>();
        for(Entry<Position, Piece> entry : pieces.entrySet()){
            pieceDtos.add(new PieceDto(roomId, entry.getValue().getName(), entry.getKey().chessCoordinate()));
        }
    }

    public PiecesDto(List<PieceDto> pieceDtos) {
        this.roomId = -1;
        this.pieceDtos = pieceDtos;
    }

    public List<PieceDto> getPieceDtos() {
        return new ArrayList<>(pieceDtos);
    }

    public int getRoomId() {
        return roomId;
    }
}
