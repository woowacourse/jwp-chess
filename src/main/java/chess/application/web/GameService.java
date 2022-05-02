package chess.application.web;

import static chess.view.Expressions.EXPRESSIONS_COLUMN;
import static chess.view.Expressions.EXPRESSIONS_ROW;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.domain.Score;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
import chess.dto.GameDto;
import chess.dto.PieceDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private static final String KEY_WINNER = "winner";
    private static final String KEY_TIE = "tie";

    private static final int INDEX_COLUMN = 0;
    private static final int INDEX_ROW = 1;

    private final GameDao gameDao;
    private final BoardDao boardDao;

    public GameService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public List<GameDto> list() {
        return gameDao.selectGames();
    }

    public void createRoom(String title, String password) {
        if (title.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("방 제목과 비밀번호를 입력하세요.");
        }
        long gameNo = gameDao.insert(GameDto.fromNewGame(title, password));
        boardDao.insert(gameNo, BoardInitializer.get().getSquares());
    }

    public ChessGame load(long gameNo) {
        List<PieceDto> rawBoard = boardDao.load(gameNo);
        Map<Position, Piece> board = rawBoard.stream()
                .collect(Collectors.toMap(
                        pieceDto2 -> parsePosition(pieceDto2.getPosition()),
                        this::parsePiece
                ));
        return ChessGame.load(board, gameDao.isWhiteTurn(gameNo));
    }

    public String loadGameTitle(long gameNo) {
        return gameDao.loadTitle(gameNo);
    }

    public Map<String, Object> modelPlayingBoard(ChessGame chessGame) {
        Map<Position, Piece> board = chessGame.getBoardSquares();
        return board.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> PieceDto.of(entry.getValue(), entry.getKey())
                ));
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

    public void move(long gameNo, ChessGame chessGame, String source, String target) {
        chessGame.move(parsePosition(source), parsePosition(target));
        save(gameNo, chessGame);
    }

    private void save(long gameNo, ChessGame chessGame) {
        gameDao.update(gameNo, chessGame.isWhiteTurn());
        boardDao.update(gameNo, chessGame.getBoardSquares());
    }

    private Position parsePosition(String rawPosition) {
        return Position.of(EXPRESSIONS_COLUMN.get(rawPosition.charAt(INDEX_COLUMN)),
                EXPRESSIONS_ROW.get(rawPosition.charAt(INDEX_ROW)));
    }

    public Map<String, Object> modelStatus(ChessGame chessGame) {
        return status(chessGame).entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
    }

    public Map<Camp, Score> status(ChessGame chessGame) {
        return chessGame.getScores();
    }

    public Map<String, Object> end(long gameNo, ChessGame chessGame) {
        gameDao.end(gameNo);
        return modelResult(chessGame);
    }

    private Map<String, Object> modelResult(ChessGame chessGame) {
        Map<String, Object> model = new HashMap<>();
        Camp winner = chessGame.getWinner();
        model.put(KEY_WINNER, winner);
        if (winner == Camp.NONE) {
            model.put(KEY_TIE, true);
        }
        return model;
    }

    public boolean isGameFinished(long gameNo, ChessGame chessGame) {
        return chessGame.isFinished() || !gameDao.isRunning(gameNo);
    }

    public void delete(long gameNo, String password) {
        checkPassword(gameNo, password);
        checkStatus(gameNo);
        boardDao.delete(gameNo);
        gameDao.delete(gameNo);
    }

    private void checkPassword(long gameNo, String password) {
        if (!gameDao.loadPassword(gameNo).equals(password)) {
            throw new IllegalArgumentException("비밀번호를 확인하세요.");
        }
    }

    private void checkStatus(long gameNo) {
        if (gameDao.isRunning(gameNo)) {
            throw new IllegalStateException("진행 중인 게임은 삭제할 수 없습니다.");
        }
    }
}
