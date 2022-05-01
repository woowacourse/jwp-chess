package chess.web.dao;

import chess.board.BoardDto;
import java.util.List;
import java.util.Optional;

public interface BoardDao {

    Long save(String turn, String title, String password);

    void updateTurnById(Long id, String turn);

    Optional<BoardDto> findById(Long id);

    List<BoardDto> findAll();

    void delete(Long id, String password);
}
