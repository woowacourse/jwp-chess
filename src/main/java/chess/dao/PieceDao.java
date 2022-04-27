package chess.dao;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;

public interface PieceDao {
    List<PieceDto> findPiecesByRoomIndex(final int roomIndex);

    void saveAllPieces(final int roomId, final Map<Position, Piece> board);

    void removePiece(final int roomId, final String position);

    void savePiece(final int roomId, final String position, final Piece piece);

    void removeAllPieces(final int roomId);
}
