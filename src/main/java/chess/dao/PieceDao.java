package chess.dao;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.Map;

public interface PieceDao {
    void save(Map<Position, Piece> board, int boardId);

    Map<Position, Piece> load(int boardId);

    boolean existPieces(int boardId);

    void delete(int boardId);

    void updatePosition(String source, String target, int boardId);
}
