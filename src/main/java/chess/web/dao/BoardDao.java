package chess.web.dao;

import chess.board.Board;
import chess.board.Turn;

import java.util.Optional;

public interface BoardDao {
    void save(Long boardId, Turn turn);

    Optional<Turn> findTurnById(Long id);

    void updateTurnById(Long id, String newTurn);

    Long save();

    // FIXME 사용하는 곳 없음
    Optional<Board> findById(Long id);

    void deleteById(Long id);

}
