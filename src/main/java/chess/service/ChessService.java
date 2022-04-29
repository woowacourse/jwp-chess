package chess.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import chess.domain.Position;
import chess.domain.game.Game;
import chess.repository.ChessRepository;
import chess.service.dto.ChessDtoAssembler;
import chess.service.dto.response.GameResponseDto;
import chess.service.dto.response.PlayerScoresResponseDto;

@Service
public class ChessService {

    private final ChessRepository chessRepository;

    public ChessService(final ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    public Map<Long, Boolean> listGames() {
        return chessRepository.findStatuses();
    }

    public Long createNewGame() {
        final Game game = chessRepository.save(Game.initializeGame());
        return game.getId();
    }

    public void removeGame(final Long gameId) {
        chessRepository.remove(gameId);
    }

    public GameResponseDto loadGame(final Long gameId) {
        return ChessDtoAssembler.gameResponseDto(chessRepository.findById(gameId));
    }

    public GameResponseDto movePiece(final Long gameId, final String source, final String target) {
        final Game game = chessRepository.findById(gameId);
        game.movePiece(Position.from(source), Position.from(target));
        return ChessDtoAssembler.gameResponseDto(chessRepository.update(game));
    }

    public GameResponseDto promotion(final Long gameId, final String pieceName) {
        final Game game = chessRepository.findById(gameId);
        game.promotePiece(pieceName);
        return ChessDtoAssembler.gameResponseDto(chessRepository.update(game));
    }

    public PlayerScoresResponseDto calculatePlayerScores(final Long gameId) {
        final Game game = chessRepository.findById(gameId);
        return ChessDtoAssembler.playerScoresResponseDto(game.getPlayerScores());
    }

    public GameResponseDto endGame(final Long gameId) {
        final Game game = chessRepository.findById(gameId);
        game.end();
        return ChessDtoAssembler.gameResponseDto(chessRepository.update(game));
    }
}
