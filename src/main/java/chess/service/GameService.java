package chess.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import chess.domain.Color;
import chess.domain.GameRepository;
import chess.domain.Position;
import chess.domain.game.Game;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Map<Long, Boolean> listGames() {
        return gameRepository.findIdAndFinished();
    }

    public Game createNewGame() {
        final Game game = Game.initializeGame();
        return gameRepository.save(game);
    }

    public Game loadGame(final Long gameId) {
        return gameRepository.findById(gameId);
    }

    public Game movePiece(final Long gameId, final String source, final String target) {
        final Game game = gameRepository.findById(gameId);
        game.movePiece(Position.from(source), Position.from(target));
        return gameRepository.update(game);
    }

    public Game promotion(final Long gameId, final String pieceName) {
        final Game game = gameRepository.findById(gameId);
        game.promotePiece(pieceName);
        return gameRepository.update(game);
    }

    public Map<Color, Double> calculatePlayerScores(final Long gameId) {
        final Game game = gameRepository.findById(gameId);
        return game.getPlayerScores();
    }

    public Game endGame(final Long gameId) {
        final Game game = gameRepository.findById(gameId);
        game.end();
        return gameRepository.update(game);
    }
}
