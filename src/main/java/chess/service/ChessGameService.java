package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.ChessmenInitializer;
import chess.dto.GameResultDto;
import chess.dto.MoveDto;
import chess.dto.PiecesDto;
import org.springframework.stereotype.Service;

@Service
public class ChessGameService {
    private final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();

    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessGameService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public void createOrGet(String gameId) {
        if (!gameDao.isInId(gameId)) {
            initGame(gameId);
        }
    }

    private void initGame(String gameId) {
        gameDao.createById(gameId);
        pieceDao.createAllById(chessmenInitializer.init().getPieces(), gameId);
    }

    public ChessGame getGameStatus(String gameId) {
        return new ChessGame(gameDao.findForceEndFlag(gameId), pieceDao.findAll(gameId),
                gameDao.findTurn(gameId));
    }

    public PiecesDto getCurrentGame(String gameId) {
        return new PiecesDto(getGameStatus(gameId).getChessmen());
    }

    public GameResultDto calculateGameResult(String gameId) {
        return new GameResultDto(GameResult.calculate(getGameStatus(gameId).getChessmen()));
    }

    public void cleanGame(String gameId) {
        pieceDao.deleteAllByGameId(gameId);
        gameDao.deleteById(gameId);
    }

    public void move(String gameId, MoveDto moveDto) {
        getGameStatus(gameId).moveChessmen(moveDto.toEntity());
        saveMove(gameId, moveDto);
    }

    private void saveMove(String gameId, MoveDto moveDto) {
        ChessGame chessGame = getGameStatus(gameId);
        chessGame.moveChessmen(moveDto.toEntity());
        pieceDao.deleteAllByGameId(gameId);
        pieceDao.createAllById(chessGame.getChessmen().getPieces(), gameId);
        gameDao.updateTurnById(chessGame.getTurn(), gameId);
        gameDao.updateForceEndFlagById(chessGame.getForceEndFlag(), gameId);
    }
}
