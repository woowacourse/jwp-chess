package chess.dao;

import chess.entity.Square;
import java.util.List;

public interface BoardDao {

    void save(List<Square> squares);

    List<Square> findById(int id);

    int update(Square square);

    void delete(int gameId);
}
