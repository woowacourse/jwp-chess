package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.Map;

public interface BoardDao {

    Map<Position, Piece> findAll(Long roomId);

    void saveAll(Map<Position, Piece> board, Long roomId);

    void delete(Long roomId);

    void updatePosition(String symbol, String position, Long roomId);
}
