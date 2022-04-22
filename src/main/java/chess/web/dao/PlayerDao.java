package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;

public interface PlayerDao {

    void save(Color color);

    Player getPlayer();

    void deleteAll();
}
