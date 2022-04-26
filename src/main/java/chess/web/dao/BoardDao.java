package chess.web.dao;

import chess.board.Turn;
import java.util.Optional;

public interface BoardDao {
    Long save(Long boardId, Turn turn);

    Optional<Turn> findTurnById(Long id);

    void updateTurnById(Long id, String newTurn);

    void deleteById(Long id);

}
