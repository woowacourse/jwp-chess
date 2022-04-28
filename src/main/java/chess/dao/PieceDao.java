package chess.dao;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.Map;

public interface PieceDao {

    void delete();

    void updatePosition(Long boardId, String source, String target);

    void save(Map<Position, Piece> board, Long boardId);

    Map<Position, Piece> load(Long boardId);
}
