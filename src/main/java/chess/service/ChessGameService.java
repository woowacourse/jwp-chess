package chess.service;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.game.ChessGame;
import chess.domain.game.GameResult;
import chess.domain.piece.ChessmenInitializer;
import chess.dto.GameResultDto;
import chess.dto.LogInDto;
import chess.dto.MoveDto;
import chess.dto.PiecesDto;
import chess.dto.RoomDto;
import java.util.List;
import java.util.stream.Collectors;
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

    public void createOrGet(LogInDto logInDto) {
        if (!gameDao.isInId(logInDto.getGameId())) {
            initGame(logInDto);
        }
        validatePassword(logInDto);
    }

    private void validatePassword(LogInDto logInDto) {
        if (!gameDao.isValidPassword(logInDto)) {
            throw new IllegalArgumentException("올바르지 않은 비밀번호 입니다.");
        }
    }

    private void initGame(LogInDto logInDto) {
        gameDao.create(logInDto);
        pieceDao.createAllById(chessmenInitializer.init().getPieces(), logInDto.getGameId());
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

    public void cleanGame(LogInDto logInDto) {
        validatePassword(logInDto);
        pieceDao.deleteAllByGameId(logInDto.getGameId());
        gameDao.deleteById(logInDto.getGameId());
    }

    public void move(String gameId, MoveDto moveDto) {
        ChessGame chessGame = getGameStatus(gameId);
        chessGame.moveChessmen(moveDto.toEntity());
        pieceDao.deleteAllByGameId(gameId);
        pieceDao.createAllById(chessGame.getChessmen().getPieces(), gameId);
        gameDao.updateTurnById(chessGame.getTurn(), gameId);
        gameDao.updateForceEndFlagById(chessGame.getForceEndFlag(), gameId);
    }

    public List<RoomDto> getRooms() {
        return gameDao.findAllGame();
    }

    public void changeToEnd(String gameId) {
        gameDao.updateForceEndFlagById(true, gameId);
    }

    public void validateEnd(String gameId) {
        if (!gameDao.findForceEndFlag(gameId)) {
            throw new IllegalArgumentException("진행중인 체스방은 삭제할 수 없습니다.");
        }
    }
}
