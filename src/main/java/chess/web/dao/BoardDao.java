package chess.web.dao;

import chess.board.Turn;
import java.util.Optional;

public interface BoardDao {

    Long save(Turn turn);

    Optional<Turn> findTurnById(Long id);

    Long update(Long boardId, Turn turn);

    void deleteById(Long id);

}
