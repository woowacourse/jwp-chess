package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;

public interface PlayerDao {

    void save(Color color);

    void saveById(int id, Color of);

    Player getPlayer();

    void deleteAll();

    Player findById(int id);

    void deleteById(int id);
}
