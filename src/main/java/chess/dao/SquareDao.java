package chess.dao;

import chess.entity.Square;
import java.util.List;

public interface SquareDao {

    int[] insertSquareAll(Long roomId, List<Square> board);

    List<Square> findSquareAllById(Long roomId);

    Long updateSquare(Square square);

    Long deleteSquareAllById(Long roomId);
}
