package chess.dao;

import chess.entity.Game;
import java.util.List;

public interface GameDao {

    int save(Game game);

    List<Game> findAll();

    Game findById(int id);

    int update(Game game);

    int delete(int id);
}
