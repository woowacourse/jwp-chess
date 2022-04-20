package chess.dao;

import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.Map;
import org.springframework.stereotype.Repository;

public interface SquareDao {

    void save(Position position, Piece piece);

    void insert2(Position position, Piece piece);

    Map<String, String> find();

    Piece find(Position position);

    void delete();
}
