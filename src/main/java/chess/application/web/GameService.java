package chess.application.web;

import static chess.view.Expressions.EXPRESSIONS_COLUMN;
import static chess.view.Expressions.EXPRESSIONS_ROW;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.dao.RoomDao;
import chess.domain.Camp;
import chess.domain.ChessGame;
import chess.domain.Room;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.Type;
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
    private final RoomDao roomDao;

    public GameService(GameDao gameDao, BoardDao boardDao, RoomDao roomDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
        this.roomDao = roomDao;
    }

    public List<Room> list() {
        return roomDao.selectAll();
    }

    public void createRoom(String title, String password) {
        if (title.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("방 제목과 비밀번호를 입력하세요.");
        }
        long roomNo = roomDao.insert(Room.create(title, password));
        ChessGame game = ChessGame.create();
        long gameNo = gameDao.insert(game, roomNo);
        boardDao.insert(gameNo, game.getBoardSquares());
    }

    public String loadGameTitle(long roomNo) {
        return roomDao.loadTitle(roomNo);
    }

    public Map<String, Object> modelPlayingBoard(long roomNo) {
        Map<Position, Piece> board = load(gameDao.findNoByRoom(roomNo)).getBoardSquares();
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

    public void move(long roomNo, String source, String target) {
        ChessGame chessGame = load(roomNo);
        chessGame.move(parsePosition(source), parsePosition(target));
        save(roomNo, chessGame);
    }

    private void save(long roomNo, ChessGame chessGame) {
        gameDao.update(roomNo, chessGame.isWhiteTurn());
        boardDao.update(gameDao.findNoByRoom(roomNo), chessGame.getBoardSquares());
    }

    private Position parsePosition(String rawPosition) {
        return Position.of(EXPRESSIONS_COLUMN.get(rawPosition.charAt(INDEX_COLUMN)),
                EXPRESSIONS_ROW.get(rawPosition.charAt(INDEX_ROW)));
    }

    public Map<String, Object> modelStatus(long roomNo) {
        return load(roomNo).getScores().entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
    }

    public Map<String, Object> end(long roomNo) {
        gameDao.end(roomNo);
        return modelResult(load(roomNo));
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

    public boolean isGameRunning(long roomNo) {
        return load(roomNo).isRunning();
    }

    private ChessGame load(long roomNo) {
        List<PieceDto> rawBoard = boardDao.load(gameDao.findNoByRoom(roomNo));
        Map<Position, Piece> board = rawBoard.stream()
                .collect(Collectors.toMap(
                        pieceDto -> parsePosition(pieceDto.getPosition()),
                        this::parsePiece
                ));
        return ChessGame.load(board, gameDao.isWhiteTurn(roomNo), gameDao.isRunning(roomNo));
    }

    public void delete(long roomNo, String password) {
        checkPassword(roomNo, password);
        checkStatus(roomNo);
        boardDao.delete(gameDao.findNoByRoom(roomNo));
        gameDao.delete(roomNo);
        roomDao.delete(roomNo);
    }

    private void checkPassword(long roomNo, String password) {
        if (!roomDao.loadPassword(roomNo).equals(password)) {
            throw new IllegalArgumentException("비밀번호를 확인하세요.");
        }
    }

    private void checkStatus(long roomNo) {
        if (gameDao.isRunning(roomNo)) {
            throw new IllegalStateException("진행 중인 게임은 삭제할 수 없습니다.");
        }
    }
}
