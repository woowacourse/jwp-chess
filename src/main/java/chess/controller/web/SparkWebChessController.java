package chess.controller.web;

import chess.controller.web.dto.game.GameRequestDto;
import chess.controller.web.dto.game.GameResponseDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.MoveRequestDto;
import chess.controller.web.dto.move.PathResponseDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.controller.web.dto.score.ScoreResponseDto;
import chess.controller.web.dto.state.StateResponseDto;
import chess.dao.jdbc.*;
import chess.domain.game.Game;
import chess.domain.movecommand.MoveCommand;
import chess.service.ChessService;
import org.modelmapper.ModelMapper;

import java.util.List;

public class SparkWebChessController {

    private final ChessService chessService;

    public SparkWebChessController() {
        this.chessService = new ChessService(
                new GameDaoJDBC(),
                new HistoryDaoJDBC(),
                new PieceDaoJDBC(),
                new ScoreDaoJDBC(),
                new StateDaoJDBC(),
                new ModelMapper()
        );
    }

    public PathResponseDto movablePath(final String source, final Long gameId) {
        return chessService.movablePath(source, gameId);
    }

    public HistoryResponseDto move(final MoveRequestDto moveRequestDto, final Long gameId) {
        MoveCommand moveCommand = new MoveCommand(moveRequestDto.getSource(), moveRequestDto.getTarget());
        return chessService.move(moveCommand, gameId);
    }

    public Long newGame(final GameRequestDto gameRequestDto) {
        Game game = Game.of(gameRequestDto.getRoomName(),
                gameRequestDto.getWhiteUsername(),
                gameRequestDto.getBlackUsername());
        return chessService.saveGame(game);
    }

    public List<PieceResponseDto> findPiecesByGameId(final Long gameId) {
        return chessService.findPiecesById(gameId);
    }

    public GameResponseDto findGameByGameId(final Long gameId) {
        return chessService.findGameByGameId(gameId);
    }

    public ScoreResponseDto findScoreByGameId(final Long gameId) {
        return chessService.findScoreByGameId(gameId);
    }

    public StateResponseDto findStateByGameId(final Long gameId) {
        return chessService.findStateByGameId(gameId);
    }

    public List<HistoryResponseDto> findHistoryByGameId(final Long gameId) {
        return chessService.findHistoryByGameId(gameId);
    }
}
