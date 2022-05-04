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
    public void saveById(int id, Color of) {

    }

    @Override
    public Player getPlayer() {
        return Player.of(repository.get(0));
    }

    @Override
    public void deleteAll() {
        repository.clear();
    }

    @Override
    public Player findById(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }
}
