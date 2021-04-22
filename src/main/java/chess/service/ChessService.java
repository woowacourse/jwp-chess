package chess.service;

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
import chess.service.dto.game.GameInfoDto;
import chess.service.dto.move.MoveDto;
import chess.service.dto.path.PathDto;
import chess.util.PieceConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Long saveGame(final GameInfoDto gameInfoDto) {
        ChessManager chessManager = new ChessManager();
        Game game = Game.of(gameInfoDto.getRoomName(), gameInfoDto.getWhiteUsername(), gameInfoDto.getBlackUsername());
        Long gameId = gameDao.save(game);
        stateDao.save(chessManager, gameId);
        scoreDao.save(chessManager.gameStatus(), gameId);
        pieceDao.savePieces(gameId, chessManager.boardToMap());
        return gameId;
    }

    public List<PieceDto> findPiecesById(final Long gameId) {
        return pieceDao.findPiecesByGameId(gameId);
    }

    public GameDto findGameByGameId(final Long gameId) {
        return gameDao.findById(gameId);
    }

    public ScoreDto findScoreByGameId(final Long gameId) {
        return scoreDao.findByGameId(gameId);
    }

    public StateDto findStateByGameId(final Long gameId) {
        return stateDao.findByGameId(gameId);
    }

    public List<HistoryDto> findHistoriesByGameId(final Long gameId) {
        return historyDao.findByGameId(gameId);
    }

    public PathDto movablePath(final String source, final Long gameId) {
        ChessManager chessManager = loadChessManager(gameId);
        Path path = chessManager.movablePath(Position.of(source));
        return PathDto.from(path);
    }

    public HistoryDto move(final MoveDto moveDto, final Long gameId) {
        ChessManager chessManager = loadChessManager(gameId);
        MoveCommand moveCommand = new MoveCommand(moveDto.getSource(), moveDto.getTarget());
        History history = History.of(moveCommand, chessManager);
        Piece sourcePiece = chessManager.pickPiece(Position.of(moveCommand.source()));
        chessManager.move(Position.of(moveCommand.source()), Position.of(moveCommand.target()));
        scoreDao.update(chessManager.gameStatus(), gameId);
        stateDao.update(chessManager, gameId);
        this.updatePieceByMove(moveCommand, sourcePiece, gameId);
        historyDao.save(history, gameId);
        return HistoryDto.from(history);
    }

    private void updatePieceByMove(MoveCommand moveCommand, Piece sourcePiece, Long gameId) {
        pieceDao.updateTargetPiece(moveCommand.target(), sourcePiece, gameId);
        pieceDao.updateSourcePiece(moveCommand.source(), gameId);
    }

    private ChessManager loadChessManager(final Long gameId) {
        PieceDtos pieceDtos = new PieceDtos(pieceDao.findPiecesByGameId(gameId));
        StateDto stateDto = stateDao.findByGameId(gameId);
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
