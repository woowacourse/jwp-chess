package chess.dao;

import chess.domain.entity.Game;
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
    public void save(String gameId, String lastTeamName, LocalDateTime createdAt) {
        Game game = new Game(gameId, lastTeamName, createdAt);

        games.add(game);
    }

    @Override
    public Game findLastGame() {
        return games.stream()
                .max(Comparator.comparing(Game::getCreatedAt))
                .get();
    }

    @Override
    public void deleteAll() {
        games.clear();
    }
}
