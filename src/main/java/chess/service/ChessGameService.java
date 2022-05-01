package chess.service;

import chess.domain.ChessGame;
import chess.domain.CurrentStatus;
import chess.domain.MovingPosition;
import chess.dto.CurrentStatusDto;
import chess.dto.MovingPositionDto;
import chess.dto.ScoreDto;
import chess.repository.ChessGameRepository;
import chess.utils.ScoreCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChessGameService {

    private static final String DUPLICATE_NAME = "채팅방 이름은 중복될 수 없습니다.";
    private static final String CANNOT_BE_DELETED = "삭제할 수 없는 상태입니다.";
    private static final String WRONG_PASSWORD = "올바르지 않은 비밀번호입니다.";

    private final ChessGameRepository repository;

    public ChessGameService(ChessGameRepository dao) {
        this.repository = dao;
    }

    public void create(String name, String password) {
        if (repository.isDuplicateName(name)) {
            throw new IllegalArgumentException(DUPLICATE_NAME);
        }
        repository.saveNewGame(name, password, new CurrentStatusDto(new CurrentStatus()));
    }

    public void start(long gameId) {
        ChessGame chessGame = findGameById(gameId);
        chessGame.start();
        repository.saveGame(gameId, chessGame.getChessBoard(), new CurrentStatusDto(chessGame.getCurrentStatus()));
    }

    public void move(long gameId, String from, String to) {
        MovingPosition movingPosition = new MovingPosition(from, to);
        ChessGame chessGame = findGameById(gameId);
        chessGame.move(movingPosition);
        repository.saveMove(gameId, new MovingPositionDto(movingPosition), new CurrentStatusDto(chessGame.getCurrentStatus()));
    }

    public void end(long gameId) {
        ChessGame chessGame = findGameById(gameId);
        chessGame.end();
        repository.saveEnd(gameId, chessGame.getStateToString());
    }

    public ScoreDto status(long gameId) {
        return ScoreCalculator.computeScore(findGameById(gameId).getChessBoard());
    }

    public List<String> getBoardByUnicode(long gameId) {
        return findGameById(gameId).getBoardByUnicode();
    }

    private ChessGame findGameById(long gameId) {
        return repository.find(gameId);
    }

    public Map<Long, String> findGameList() {
        return repository.findGameList();
    }

    public void delete(long gameId, String password) {
        validateState(gameId);
        validatePassword(gameId, password);

        repository.delete(gameId);
    }

    private void validateState(long gameId) {
        if (!repository.find(gameId).canBeDeleted()) {
            throw new IllegalArgumentException(CANNOT_BE_DELETED);
        }
    }

    private void validatePassword(long gameId, String password) {
        if (!password.equals(repository.findPasswordById(gameId))) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
    }
}
