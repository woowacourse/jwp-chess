package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import java.util.Map;

public interface ChessBoardDao {

    void save(Position position, Piece piece);

    void saveById(int id, Position position, Piece piece);

    void deleteAll();

    void deleteById(int id);

    Map<Position, Piece> findAll();

    Map<Position, Piece> findById(int id);
}
