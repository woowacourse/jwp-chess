package chess.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;

import chess.domain.ChessRepository;
import chess.domain.game.Game;
import chess.domain.game.state.GameStateFactory;
import chess.repository.dto.game.GameStatus;

public class FakeChessRepository implements ChessRepository {

    private final Map<Long, Game> games = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public Game save(final Game game) {
        final Game newGame = createNewGame(autoId++, game);
        games.put(newGame.getId(), newGame);
        return newGame;
    }

    private Game createNewGame(final Long gameId, final Game game) {
        return Game.loadGame(
                gameId, game.getTitle(), game.getPassword(),
                GameStateFactory.loadGameState(
                        game.getPlayers(), game.isFinished(), game.getColorOfCurrentTurn())
        );
    }

    @Override
    public Game findById(final Long gameId) {
        if (games.containsKey(gameId)) {
            return games.get(gameId);
        }
        throw new EmptyResultDataAccessException(1);
    }

    @Override
    public List<GameStatus> findStatuses() {
        return games.values()
                .stream()
                .map(game -> new GameStatus(
                        game.getId(),
                        game.getTitle(),
                        game.isFinished())
                )
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Game update(final Game game) {
        games.put(game.getId(), game);
        return game;
    }

    @Override
    public void remove(final Long gameId) {
        games.remove(gameId);
    }
}
