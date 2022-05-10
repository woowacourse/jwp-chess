package chess.application.web;

import static chess.view.Expressions.EXPRESSIONS_COLUMN;
import static chess.view.Expressions.EXPRESSIONS_ROW;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.domain.board.Position;
import chess.domain.gamestate.Score;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import chess.dto.GameDto;
import chess.dto.PieceDto;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import spark.Request;

@Service
public class GameService {
    private static final String KEY_READY = "ready";
    private static final String KEY_STARTED = "started";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_TARGET = "target";
    private static final String KEY_WINNER = "winner";
    private static final String KEY_TIE = "tie";

    private static final String REGEX_VALUE = "=";
    private static final String REGEX_DATA = "&";
    private static final String ERROR_PASSWORD = "비밀번호가 일치하지 않습니다.";
    private static final String ERROR_IS_RUNNING = "실행 중인 게임은 삭제할 수 없습니다.";

    private static final int INDEX_KEY = 0;
    private static final int INDEX_VALUE = 1;
    private static final int INDEX_COLUMN = 0;
    private static final int INDEX_ROW = 1;

    private final ChessGame chessGame;
    private final GameDao gameDao;
    private final BoardDao boardDao;

    public GameService(GameDao gameDao, BoardDao boardDao) {
        this.chessGame = new ChessGame();
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public List<GameDto> findAllGames() {
        return gameDao.findAll();
    }

    public Map<String, Object> modelPlayingBoard() {
        Map<Position, Piece> board = chessGame.getBoardSquares();
        Map<String, Object> model = board.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> PieceDto.of(entry.getValue(), entry.getKey())
                ));
        model.put(KEY_STARTED, true);
        model.put(KEY_READY, false);
        return model;
    }

    public Long create(String title, String password) {
        ChessGame chessGame = new ChessGame(title, password);
        chessGame.start();
        Long gameId = gameDao.save(chessGame);
        boardDao.saveAll(gameId, chessGame.getBoardSquares());
        return gameId;
    }

    public Map<String, Object> findBoardByGameId(Long id) {
        List<PieceDto> rawBoard = boardDao.findAllByGameId(id);
        Map<Position, Piece> board = rawBoard.stream()
                .collect(Collectors.toMap(
                        pieceDto -> parsePosition(pieceDto.getPosition()),
                        this::parsePiece
                ));
        chessGame.load(board, gameDao.isWhiteTurn(id));
        return modelPlayingBoard();
    }

    private Piece parsePiece(PieceDto piece) {
        String rawType = piece.getType();
        if (rawType.isBlank()) {
            rawType = "none";
        }
        Type type = Type.valueOf(rawType.toUpperCase());
        Camp camp = Camp.valueOf(piece.getCamp().toUpperCase());
        return type.generatePiece(camp);
    }

    public void move(Request req) {
        Map<String, String> positions = Arrays.stream(req.body().split(REGEX_DATA))
                .map(data -> data.split(REGEX_VALUE))
                .collect(Collectors.toMap(
                        data -> data[INDEX_KEY],
                        data -> data[INDEX_VALUE]
                ));
        chessGame.move(parsePosition(positions.get(KEY_SOURCE)), parsePosition(positions.get(KEY_TARGET)));
    }

    public void move(String source, String target) {
        chessGame.move(parsePosition(source), parsePosition(target));
    }

    private Position parsePosition(String rawPosition) {
        return Position.of(EXPRESSIONS_COLUMN.get(rawPosition.charAt(INDEX_COLUMN)),
                EXPRESSIONS_ROW.get(rawPosition.charAt(INDEX_ROW)));
    }

    public Map<String, Object> modelStatus() {
        Map<Camp, Score> scores = chessGame.getScores();
        return scores.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
    }

    public Map<Camp, Score> status() {
        return chessGame.getScores();
    }

    public void updateGame(Long id) {
        gameDao.updateTurnById(id);
        boardDao.deleteAllByGameId(id);
        boardDao.saveAll(id, chessGame.getBoardSquares());
    }

    public Map<String, Object> end(Long id) {
        chessGame.end();
        gameDao.updateStateById(id);
        return modelResult();
    }

    private Map<String, Object> modelResult() {
        Map<String, Object> model = new HashMap<>();
        Camp winner = chessGame.getWinner();
        model.put(KEY_WINNER, winner);
        if (winner == Camp.NONE) {
            model.put(KEY_TIE, true);
        }
        model.put(KEY_STARTED, false);
        model.put(KEY_READY, true);
        return model;
    }

    public boolean isGameFinished() {
        return this.chessGame.isFinished();
    }

    public Map<Position, Piece> getBoard() {
        return chessGame.getBoardSquares();
    }

    public void deleteGame(Long id, Map<String, String> request) {
        String password = request.get("password");
        ChessGame savedChessGame = gameDao.findById(id);
        if (savedChessGame.incorrectPassword(password)) {
            throw new IllegalArgumentException(ERROR_PASSWORD);
        }
        if (gameDao.findRunningById(id)) {
            throw new IllegalArgumentException(ERROR_IS_RUNNING);
        }
        boardDao.deleteAllByGameId(id);
        gameDao.deleteById(id);
    }
}