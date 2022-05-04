package chess.service;

import chess.dao.BoardDao;
import chess.dao.CurrentStatusDao;
import chess.dao.GameDao;
import chess.domain.ChessGame;
import chess.domain.CurrentStatus;
import chess.domain.MovingPosition;
import chess.dto.CurrentStatusDto;
import chess.dto.MovingPositionDto;
import chess.dto.ScoreDto;
import chess.utils.ScoreCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChessGameService {

    private static final String DUPLICATE_NAME = "채팅방 이름은 중복될 수 없습니다.";
    private static final String CANNOT_BE_DELETED = "삭제할 수 없는 상태입니다.";
    private static final String WRONG_PASSWORD = "올바르지 않은 비밀번호입니다.";

    private final BoardDao boardDao;
    private final CurrentStatusDao currentStatusDao;
    private final GameDao gameDao;

    public ChessGameService(BoardDao boardDao, CurrentStatusDao currentStatusDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.currentStatusDao = currentStatusDao;
        this.gameDao = gameDao;
    }

    public void create(String name, String password) {
        if (gameDao.isDuplicateName(name)) {
            throw new IllegalArgumentException(DUPLICATE_NAME);
        }

        int gameId = gameDao.saveGame(name, password);
        currentStatusDao.save(gameId, new CurrentStatusDto(new CurrentStatus()));
    }

    public Map<Integer, String> findGameList() {
        return gameDao.findGameList();
    }

    public void delete(int gameId, String password) {
        validateState(gameId);
        validatePassword(gameId, password);

        gameDao.delete(gameId);
    }

    public void start(int gameId) {
        ChessGame chessGame = findGameById(gameId);
        chessGame.start();

        boardDao.saveBoard(gameId, chessGame.getChessBoard());
        currentStatusDao.update(gameId, new CurrentStatusDto(chessGame.getCurrentStatus()));
    }

    public void move(int gameId, String from, String to) {
        MovingPosition movingPosition = new MovingPosition(from, to);
        ChessGame chessGame = findGameById(gameId);
        chessGame.move(movingPosition);

        boardDao.saveMove(gameId, new MovingPositionDto(movingPosition));
        currentStatusDao.update(gameId, new CurrentStatusDto(chessGame.getCurrentStatus()));
    }

    public void end(int gameId) {
        ChessGame chessGame = findGameById(gameId);
        chessGame.end();

        currentStatusDao.saveState(gameId, chessGame.getStateToString());
    }

    public ScoreDto status(int gameId) {
        return ScoreCalculator.computeScore(findGameById(gameId).getChessBoard());
    }

    public List<String> getBoardByUnicode(int gameId) {
        return findGameById(gameId).getBoardByUnicode();
    }

    private ChessGame findGameById(int gameId) {
        return new ChessGame(currentStatusDao.findByGameId(gameId), boardDao.findByGameId(gameId));
    }

    private void validateState(int gameId) {
        if (findGameById(gameId).canBeDeleted()) {
            throw new IllegalArgumentException(CANNOT_BE_DELETED);
        }
    }

    private void validatePassword(int gameId, String password) {
        if (!password.equals(gameDao.findPasswordById(gameId))) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
    }
}
