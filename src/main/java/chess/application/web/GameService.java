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
        long roomNo = roomDao.insert(Room.create(title, password));
        ChessGame game = ChessGame.create();
        long gameNo = gameDao.insert(game, roomNo);
        boardDao.insert(gameNo, game.getBoardSquares());
    }

    public String loadGameTitle(long roomNo) {
        return roomDao.loadTitle(roomNo);
    }

    public Map<String, Object> modelPlayingBoard(long roomNo) {
        Map<Position, Piece> board = loadGame(gameDao.findNoByRoom(roomNo)).getBoardSquares();
        return board.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> PieceDto.of(entry.getValue(), entry.getKey())
                ));
    }

    public void move(long roomNo, String source, String target) {
        ChessGame chessGame = loadGame(roomNo);
        chessGame.move(parsePosition(source), parsePosition(target));
        save(roomNo, chessGame);
    }

    private Position parsePosition(String rawPosition) {
        return Position.of(EXPRESSIONS_COLUMN.get(rawPosition.charAt(INDEX_COLUMN)),
                EXPRESSIONS_ROW.get(rawPosition.charAt(INDEX_ROW)));
    }

    private void save(long roomNo, ChessGame chessGame) {
        gameDao.update(roomNo, chessGame.isWhiteTurn());
        boardDao.update(gameDao.findNoByRoom(roomNo), chessGame.getBoardSquares());
    }

    public Map<String, Object> modelStatus(long roomNo) {
        return loadGame(roomNo).getScores().entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
    }

    public Map<String, Object> end(long roomNo) {
        Room room = roomDao.load(roomNo);
        room.end();
        roomDao.update(room);
        return modelResult(loadGame(roomNo));
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
        Room room = roomDao.load(roomNo);
        return room.isRunning() && loadGame(roomNo).isRunning();
    }

    private ChessGame loadGame(long roomNo) {
        List<PieceDto> rawBoard = boardDao.load(gameDao.findNoByRoom(roomNo));
        Map<Position, Piece> board = rawBoard.stream()
                .collect(Collectors.toMap(
                        pieceDto -> parsePosition(pieceDto.getPosition()),
                        PieceDto::getPieceAsObject
                ));
        return ChessGame.load(board, gameDao.isWhiteTurn(roomNo));
    }

    public void delete(long roomNo, String password) {
        Room room = roomDao.load(roomNo);
        checkPassword(room, password);
        checkStatus(room);
        boardDao.delete(gameDao.findNoByRoom(roomNo));
        gameDao.delete(roomNo);
        roomDao.delete(roomNo);
    }

    private void checkPassword(Room room, String password) {
        if (!room.isPassword(password)) {
            throw new IllegalArgumentException("비밀번호를 확인하세요.");
        }
    }

    private void checkStatus(Room room) {
        if (room.isRunning()) {
            throw new IllegalStateException("진행 중인 게임은 삭제할 수 없습니다.");
        }
    }
}
