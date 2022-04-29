package chess.dao;

import chess.domain.db.Game;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FakeGameDao implements GameDao{

    private final List<Game> games = new ArrayList<>();


    @Override
    public boolean isExistGame() {
        return games.size() > 0;
    }

    @Override
    public void save(String gameId, String lastTeamName) {
        Game game = new Game(gameId, lastTeamName, LocalDateTime.now());

        games.add(game);
    }

    @Override
    public Game findLastGame() {
        return games.stream()
                .max(Comparator.comparing(Game::getCreateAt))
                .get();
    }

    @Override
    public void deleteAll() {
        games.clear();
    }
}
