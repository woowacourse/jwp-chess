package chess.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import chess.domain.Position;
import chess.domain.game.Game;
import chess.repository.GameRepository;
import chess.service.dto.ChessDtoAssembler;
import chess.service.dto.response.GameResponseDto;
import chess.service.dto.response.PlayerScoresResponseDto;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Map<Long, Boolean> listGames() {
        return gameRepository.findIdAndFinished();
    }

    public Long createNewGame() {
        final Game game = gameRepository.save(Game.initializeGame());
        return game.getId();
    }

    public GameResponseDto loadGame(final Long gameId) {
        return ChessDtoAssembler.gameResponseDto(gameRepository.findById(gameId));
    }

    public GameResponseDto movePiece(final Long gameId, final String source, final String target) {
        final Game game = gameRepository.findById(gameId);
        game.movePiece(Position.from(source), Position.from(target));
        return ChessDtoAssembler.gameResponseDto(gameRepository.update(game));
    }

    public GameResponseDto promotion(final Long gameId, final String pieceName) {
        final Game game = gameRepository.findById(gameId);
        game.promotePiece(pieceName);
        return ChessDtoAssembler.gameResponseDto(gameRepository.update(game));
    }

    public PlayerScoresResponseDto calculatePlayerScores(final Long gameId) {
        final Game game = gameRepository.findById(gameId);
        return ChessDtoAssembler.playerScoresResponseDto(game.getPlayerScores());
    }

    public GameResponseDto endGame(final Long gameId) {
        final Game game = gameRepository.findById(gameId);
        game.end();
        return ChessDtoAssembler.gameResponseDto(gameRepository.update(game));
    }
}
