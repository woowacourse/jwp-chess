package chess.dao;

import chess.domain.state.State;
import chess.entity.Game;
import java.util.List;

public interface GameDao {

    int save(Game game);

    List<Game> findAll();

    Game findById(int id);

    State findState(int id);

    int update(String state, int id);

    int delete(int id);
}
