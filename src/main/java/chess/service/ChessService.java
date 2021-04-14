package chess.service;

import chess.controller.web.dto.game.GameResponseDto;
import chess.controller.web.dto.history.HistoryResponseDto;
import chess.controller.web.dto.move.PathResponseDto;
import chess.controller.web.dto.piece.PieceResponseDto;
import chess.controller.web.dto.piece.PieceResponseDtos;
import chess.controller.web.dto.score.ScoreResponseDto;
import chess.controller.web.dto.state.StateResponseDto;
import chess.dao.*;
import chess.domain.board.position.Path;
import chess.domain.board.position.Position;
import chess.domain.game.Game;
import chess.domain.history.History;
import chess.domain.manager.ChessManager;
import chess.domain.movecommand.MoveCommand;
import chess.domain.piece.Owner;
import chess.domain.piece.Piece;

import java.util.List;

public class ChessService {

    private final GameDaoJDBC gameDaoJDBC;
    private final HistoryDaoJDBC historyDaoJDBC;
    private final PieceDaoJDBC pieceDaoJDBC;
    private final ScoreDaoJDBC scoreDaoJDBC;
    private final StateDaoJDBC stateDaoJDBC;

    public ChessService() {
        this.gameDaoJDBC = new GameDaoJDBC();
        this.historyDaoJDBC = new HistoryDaoJDBC();
        this.pieceDaoJDBC = new PieceDaoJDBC();
        this.scoreDaoJDBC = new ScoreDaoJDBC();
        this.stateDaoJDBC = new StateDaoJDBC();
    }

    public Long saveGame(final Game game) {
        ChessManager chessManager = new ChessManager();
        Long gameId = gameDaoJDBC.saveGame(game);
        stateDaoJDBC.saveState(chessManager, gameId);
        scoreDaoJDBC.saveScore(chessManager.gameStatus(), gameId);
        pieceDaoJDBC.savePieces(gameId, chessManager.boardToMap());
        return gameId;

    }

    public List<PieceResponseDto> findPiecesById(final Long gameId) {
        return pieceDaoJDBC.findPiecesByGameId(gameId);
    }

    public GameResponseDto findGameByGameId(final Long gameId) {
        return gameDaoJDBC.findGameById(gameId);
    }

    public ScoreResponseDto findScoreByGameId(final Long gameId) {
        return scoreDaoJDBC.findScoreByGameId(gameId);
    }

    public StateResponseDto findStateByGameId(final Long gameId) {
        return stateDaoJDBC.findStateByGameId(gameId);
    }

    public List<HistoryResponseDto> findHistoryByGameId(final Long gameId) {
        return historyDaoJDBC.findHistoryByGameId(gameId);
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
        scoreDaoJDBC.updateScore(chessManager.gameStatus(), gameId);
        stateDaoJDBC.updateState(chessManager, gameId);
        this.updatePieceByMove(moveCommand, sourcePiece, gameId);
        historyDaoJDBC.saveHistory(history, gameId);
        return HistoryResponseDto.from(history);
    }

    private void updatePieceByMove(MoveCommand moveCommand, Piece sourcePiece, Long gameId) {
        pieceDaoJDBC.updateTargetPiece(moveCommand.target(), sourcePiece, gameId);
        pieceDaoJDBC.updateSourcePiece(moveCommand.source(), gameId);
    }

    private ChessManager loadChessManager(final Long gameId) {
        PieceResponseDtos pieceResponseDtos = new PieceResponseDtos(pieceDaoJDBC.findPiecesByGameId(gameId));
        StateResponseDto stateResponseDto = stateDaoJDBC.findStateByGameId(gameId);
        return new ChessManager(
                pieceResponseDtos.toBoard(),
                Owner.valueOf(stateResponseDto.getTurnOwner()),
                stateResponseDto.getTurnNumber(),
                stateResponseDto.isPlaying());
    }
}
