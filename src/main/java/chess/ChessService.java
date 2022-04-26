package chess;

import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.ChessBoard;
import chess.domain.Status;
import chess.domain.WebChessGame;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.RoomCreationDto;
import chess.entity.Room;
import chess.entity.Square;
import chess.utils.PieceFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private static final String NO_ROOM_MESSAGE = "해당 ID와 일치하는 Room이 존재하지 않습니다.";
    private static final String NO_SQUARE_MESSAGE = "해당 방, 위치에 존재하는 Square가 없습니다.";
    private static final String NO_SQUARES_MESSAGE = "해당 ID에 체스게임이 초기화되지 않았습니다.";
    private static final String ROOM_NAME_DUP_MESSAGE = "해당 이름의 방이 이미 존재합니다.";

    private final RoomDao roomDao;
    private final SquareDao squareDao;

    public ChessService(RoomDao roomDao, SquareDao squareDao) {
        this.roomDao = roomDao;
        this.squareDao = squareDao;
    }

    public BoardDto startNewGame(String roomName) {
        Room room = roomDao.findByName(roomName)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));

        WebChessGame webChessGame = new WebChessGame();
        webChessGame.start();
        squareDao.removeAll(room.getId());
        Map<Position, Piece> board = webChessGame.getBoard();
        List<Square> squares = convertBoardToSquares(board);
        squareDao.saveAll(squares, room.getId());
        roomDao.updateTurn(room.getId(), webChessGame.getTurn());
        return BoardDto.of(board, webChessGame.getTurn());
    }

    public BoardDto load(String roomName) {
        Room room = roomDao.findByName(roomName)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessBoard chessBoard = loadChessBoard(room.getId());

        return BoardDto.of(chessBoard.getPieces(), room.getTurn());
    }

    public BoardDto move(String roomName, MoveDto moveDto) {
        Room room = roomDao.findByName(roomName)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        WebChessGame webChessGame = WebChessGame.of(loadChessBoard(room.getId()), room.getTurn());
        webChessGame.move(moveDto.getFrom(), moveDto.getTo());
        roomDao.updateTurn(room.getId(), webChessGame.getTurn());
        updateMovement(room.getId(), moveDto);

        return BoardDto.of(webChessGame.getBoard(), webChessGame.getTurn());
    }

    private void updateMovement(long roomId, MoveDto moveDto) {
        String fromPiece = squareDao.findByRoomIdAndPosition(roomId, moveDto.getFrom())
                .orElseThrow(() -> new NoSuchElementException(NO_SQUARE_MESSAGE))
                .getPiece();
        squareDao.update(roomId, moveDto.getFrom(), "empty");
        squareDao.update(roomId, moveDto.getTo(), fromPiece);
    }

    private List<Square> convertBoardToSquares(Map<Position, Piece> board) {
        return board.keySet().stream()
                .map(position -> new Square(position.convertToString(), board.get(position).convertToString()))
                .collect(Collectors.toList());
    }

    private ChessBoard loadChessBoard(long roomId) {
        List<Square> squares = squareDao.findByRoomId(roomId);
        if (squares.isEmpty()) {
            throw new NoSuchElementException(NO_SQUARES_MESSAGE);
        }
        Map<Position, Piece> board = new HashMap<>();
        for (Square square : squares) {
            Position position = Position.of(square.getPosition());
            Piece piece = PieceFactory.convertToPiece(square.getPiece());
            board.put(position, piece);
        }
        return new ChessBoard(() -> board);
    }

    public Status status(String roomName) {
        Room room = roomDao.findByName(roomName)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessBoard chessBoard = loadChessBoard(room.getId());
        WebChessGame webChessGame = WebChessGame.of(chessBoard, room.getTurn());

        return webChessGame.status();
    }

    public long createRoom(RoomCreationDto roomCreationDto) {
        Room newRoom = new Room(roomCreationDto.getName(), roomCreationDto.getPassword());
        return roomDao.save(newRoom);
    }
}
