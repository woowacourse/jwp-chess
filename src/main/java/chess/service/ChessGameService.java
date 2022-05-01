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

    public void start(int gameId) {
        ChessGame chessGame = findGameById(gameId);
        chessGame.start();
        repository.saveGame(gameId, chessGame.getChessBoard(), new CurrentStatusDto(chessGame.getCurrentStatus()));
    }

    public void move(int gameId, String from, String to) {
        MovingPosition movingPosition = new MovingPosition(from, to);
        ChessGame chessGame = findGameById(gameId);
        chessGame.move(movingPosition);
        repository.saveMove(gameId, new MovingPositionDto(movingPosition), new CurrentStatusDto(chessGame.getCurrentStatus()));
    }

    public void end(int gameId) {
        ChessGame chessGame = findGameById(gameId);
        chessGame.end();
        repository.saveEnd(gameId, chessGame.getStateToString());
    }

    public ScoreDto status(int gameId) {
        return ScoreCalculator.computeScore(findGameById(gameId).getChessBoard());
    }

    public List<String> getBoardByUnicode(int gameId) {
        return findGameById(gameId).getBoardByUnicode();
    }

    private ChessGame findGameById(int gameId) {
        return repository.find(gameId);
    }

    public Map<Integer, String> findGameList() {
        return repository.findGameList();
    }
}
