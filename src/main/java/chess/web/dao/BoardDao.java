package chess.web.dao;

import chess.board.BoardEntity;
import java.util.List;
import java.util.Optional;

public interface BoardDao {

    Long save(String turn, String title, String password);

    void updateTurnById(Long id, String turn);

    Optional<BoardEntity> findById(Long id);

    List<BoardEntity> findAll();

    void delete(Long id, String password);
}
