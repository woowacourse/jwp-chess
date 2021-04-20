package chess.service;

import chess.controller.web.dto.game.GameResponseDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.PathResponseDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.controller.web.dto.score.ScoreResponseDto;
import chess.controller.web.dto.state.StateResponseDto;
import chess.dao.*;
import chess.dao.dto.game.GameDto;
import chess.dao.dto.history.HistoryDto;
import chess.dao.dto.piece.PieceDto;
import chess.dao.dto.piece.PieceDtos;
import chess.dao.dto.score.ScoreDto;
import chess.dao.dto.state.StateDto;
import chess.domain.board.Board;
import chess.domain.board.position.Path;
import chess.domain.board.position.Position;
import chess.domain.game.Game;
import chess.domain.history.History;
import chess.domain.manager.ChessManager;
import chess.domain.movecommand.MoveCommand;
import chess.domain.piece.Owner;
import chess.domain.piece.Piece;
import chess.util.PieceConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final GameDao gameDao;
    private final HistoryDao historyDao;
    private final PieceDao pieceDao;
    private final ScoreDao scoreDao;
    private final StateDao stateDao;
    private final ModelMapper modelMapper;

    public ChessService(GameDao gameDao, HistoryDao historyDao, PieceDao pieceDao, ScoreDao scoreDao,
                        StateDao stateDao, ModelMapper modelMapper) {
        this.gameDao = gameDao;
        this.historyDao = historyDao;
        this.pieceDao = pieceDao;
        this.scoreDao = scoreDao;
        this.stateDao = stateDao;
        this.modelMapper = modelMapper;
    }

    public Long saveGame(final Game game) {
        ChessManager chessManager = new ChessManager();
        Long gameId = gameDao.saveGame(game);
        stateDao.saveState(chessManager, gameId);
        scoreDao.saveScore(chessManager.gameStatus(), gameId);
        pieceDao.savePieces(gameId, chessManager.boardToMap());
        return gameId;
    }

    public List<PieceResponseDto> findPiecesById(final Long gameId) {
        List<PieceDto> pieceDtos = pieceDao.findPiecesByGameId(gameId);
        return pieceDtos.stream()
                .map(pieceDto -> modelMapper.map(pieceDto, PieceResponseDto.class))
                .collect(Collectors.toList());
    }

    public GameResponseDto findGameByGameId(final Long gameId) {
        GameDto gameDto = gameDao.findGameById(gameId);
        return modelMapper.map(gameDto, GameResponseDto.class);
    }

    public ScoreResponseDto findScoreByGameId(final Long gameId) {
        ScoreDto scoreDto = scoreDao.findScoreByGameId(gameId);
        return modelMapper.map(scoreDto, ScoreResponseDto.class);
    }

    public StateResponseDto findStateByGameId(final Long gameId) {
        StateDto stateDto = stateDao.findStateByGameId(gameId);
        return modelMapper.map(stateDto, StateResponseDto.class);
    }

    public List<HistoryResponseDto> findHistoryByGameId(final Long gameId) {
        List<HistoryDto> historyDtos = historyDao.findHistoryByGameId(gameId);
        return historyDtos.stream()
                .map(historyDto -> modelMapper.map(historyDto, HistoryResponseDto.class))
                .collect(Collectors.toList());
    }

    public PathResponseDto movablePath(final String source, final Long gameId) {
        ChessManager chessManager = loadChessManager(gameId);
        Path path = chessManager.movablePath(Position.of(source));
        return PathResponseDto.from(path);
    }

    public HistoryResponseDto move(final MoveCommand moveCommand, final Long gameId) {
        ChessManager chessManager = loadChessManager(gameId);
        History history = History.of(moveCommand, chessManager);
        Piece sourcePiece = chessManager.pickPiece(Position.of(moveCommand.source()));
        chessManager.move(Position.of(moveCommand.source()), Position.of(moveCommand.target()));
        scoreDao.updateScore(chessManager.gameStatus(), gameId);
        stateDao.updateState(chessManager, gameId);
        this.updatePieceByMove(moveCommand, sourcePiece, gameId);
        historyDao.saveHistory(history, gameId);
        return modelMapper.map(HistoryDto.from(history), HistoryResponseDto.class);
    }

    private void updatePieceByMove(MoveCommand moveCommand, Piece sourcePiece, Long gameId) {
        pieceDao.updateTargetPiece(moveCommand.target(), sourcePiece, gameId);
        pieceDao.updateSourcePiece(moveCommand.source(), gameId);
    }

    private ChessManager loadChessManager(final Long gameId) {
        PieceDtos pieceDtos = new PieceDtos(pieceDao.findPiecesByGameId(gameId));
        StateDto stateDto = stateDao.findStateByGameId(gameId);
        return new ChessManager(
                this.initBoard(pieceDtos),
                Owner.valueOf(stateDto.getTurnOwner()),
                stateDto.getTurnNumber(),
                stateDto.isPlaying());
    }

    private Board initBoard(PieceDtos pieceDtos) {
        Map<Position, Piece> pieces = new HashMap<>();
        for (PieceDto pieceDto : pieceDtos.getPieces()) {
            pieces.put(Position.of(pieceDto.getPosition()), PieceConverter.parsePiece(pieceDto.getSymbol()));
        }
        return new Board(pieces);
    }
}
