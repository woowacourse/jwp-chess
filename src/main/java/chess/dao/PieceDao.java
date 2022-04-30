package chess.dao;

import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.dto.PieceDto;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceDao {

    void saveAllPieces(final int roomNumber, final Map<Position, Piece> board);

    List<PieceDto> findAllPieces(final int roomNumber);

    void removePieceByPosition(final int roomNumber, final String position);

    void savePiece(final int roomNumber, final String position, final Piece piece);

    void removeAllPieces(final int roomNumber);
}
