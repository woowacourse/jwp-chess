package chess.web.service.fakedao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.dao.PlayerDao;
import java.util.ArrayList;
import java.util.List;

public class FakePlayerDao implements PlayerDao {

    List<Color> repository = new ArrayList<>() {{
        add(Color.WHITE);
    }};

    @Override
    public void save(Color color) {
        repository.add(color);
    }

    @Override
    public Player getPlayer() {
        return Player.of(repository.get(0));
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }
}
