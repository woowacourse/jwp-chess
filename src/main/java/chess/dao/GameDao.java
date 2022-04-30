package chess.dao;

import chess.domain.state.State;
import chess.dto.GameDto;
import java.util.List;

public interface GameDao {

    int save(String title, String password, String state);

    List<GameDto> findAll();

    State findState(int id);

    String findPassword(int id);

    Long findGameCount();

    int update(String state, int id);

    int delete(int id);
}
