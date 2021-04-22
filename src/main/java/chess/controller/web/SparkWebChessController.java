package chess.controller.web;

import chess.controller.web.dto.game.GameRequestDto;
import chess.controller.web.dto.game.GameResponseDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.MoveRequestDto;
import chess.controller.web.dto.move.PathResponseDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.controller.web.dto.score.ScoreResponseDto;
import chess.controller.web.dto.state.StateResponseDto;
import chess.dao.dto.game.GameDto;
import chess.dao.dto.history.HistoryDto;
import chess.dao.dto.piece.PieceDto;
import chess.dao.dto.score.ScoreDto;
import chess.dao.dto.state.StateDto;
import chess.dao.jdbc.*;
import chess.service.ChessService;
import chess.service.dto.game.GameInfoDto;
import chess.service.dto.move.MoveDto;
import chess.service.dto.path.PathDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SparkWebChessController {

    private final ChessService chessService;
    private final ModelMapper modelMapper;

    public SparkWebChessController() {
        this.chessService = new ChessService(
                new GameDaoJDBC(),
                new HistoryDaoJDBC(),
                new PieceDaoJDBC(),
                new ScoreDaoJDBC(),
                new StateDaoJDBC(),
                new ModelMapper()
        );
        this.modelMapper = new ModelMapper();
    }

    public PathResponseDto movablePath(final String source, final Long gameId) {
        PathDto pathDto = chessService.movablePath(source, gameId);
        return modelMapper.map(pathDto, PathResponseDto.class);
    }

    public HistoryResponseDto move(final MoveRequestDto moveRequestDto, final Long gameId) {
        MoveDto moveDto = modelMapper.map(moveRequestDto, MoveDto.class);
        HistoryDto historyDto = chessService.move(moveDto, gameId);
        return modelMapper.map(historyDto, HistoryResponseDto.class);
    }

    public Long newGame(final GameRequestDto gameRequestDto) {
        GameInfoDto gameInfoDto = modelMapper.map(gameRequestDto, GameInfoDto.class);
        return chessService.saveGame(gameInfoDto);
    }

    public List<PieceResponseDto> findPiecesByGameId(final Long gameId) {
        List<PieceDto> pieceDtos = chessService.findPiecesById(gameId);
        return pieceDtos.stream()
                .map(pieceDto -> modelMapper.map(pieceDto, PieceResponseDto.class))
                .collect(Collectors.toList());
    }

    public GameResponseDto findGameByGameId(final Long gameId) {
        GameDto gameDto = chessService.findGameByGameId(gameId);
        return modelMapper.map(gameDto, GameResponseDto.class);
    }

    public ScoreResponseDto findScoreByGameId(final Long gameId) {
        ScoreDto scoreDto = chessService.findScoreByGameId(gameId);
        return modelMapper.map(scoreDto, ScoreResponseDto.class);
    }

    public StateResponseDto findStateByGameId(final Long gameId) {
        StateDto stateDto = chessService.findStateByGameId(gameId);
        return modelMapper.map(stateDto, StateResponseDto.class);
    }

    public List<HistoryResponseDto> findHistoryByGameId(final Long gameId) {
        List<HistoryDto> historyDtos = chessService.findHistoriesByGameId(gameId);
        return historyDtos.stream()
                .map(historyDto -> modelMapper.map(historyDto, HistoryResponseDto.class))
                .collect(Collectors.toList());
    }
}
