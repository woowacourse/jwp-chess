package chess;

import static chess.domain.piece.Color.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.Status;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.RoomDto;
import chess.entity.Room;
import chess.entity.Square;
import chess.utils.PieceFactory;

@Service
public class ChessService {

    private static final String NO_ROOM_MESSAGE = "해당 ID와 일치하는 Room이 존재하지 않습니다.";
    private static final String NO_SQUARE_MESSAGE = "해당 방, 위치에 존재하는 Square가 없습니다.";
    private static final String NO_SQUARES_MESSAGE = "해당 ID에 체스게임이 초기화되지 않았습니다.";
    private static final String INVALID_PASSWORD_MESSAGE = "Password가 일치하지 않습니다.";
    private static final String DELETE_NOT_ALLOWED_WHEN_RUNNING_MESSAGE = "진행중인 방은 삭제할 수 없습니다.";
    private static final String DUPLICATED_ROOM_NAME_MESSAGE = "중복된 방 이름입니다.";

    private final RoomDao roomDao;
    private final SquareDao squareDao;

    public ChessService(RoomDao roomDao, SquareDao squareDao) {
        this.roomDao = roomDao;
        this.squareDao = squareDao;
    }

    @Transactional(rollbackFor = Exception.class)
    public BoardDto startNewGame(long roomId) {
        Room room = roomDao.findById(roomId)
            .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));

        ChessGame chessGame = new ChessGame();
        chessGame.start();
        Map<Position, Piece> board = chessGame.getBoard();
        List<Square> squares = convertBoardToSquares(board);
        squareDao.removeAll(room.getId());
        squareDao.saveAll(squares, room.getId());
        roomDao.update(room.getId(), chessGame.getTurn());
        return BoardDto.of(board, chessGame.getTurn());
    }

    public BoardDto findRoom(long roomId) {
        Room room = roomDao.findById(roomId)
            .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessBoard chessBoard = loadChessBoard(room.getId());

        return BoardDto.of(chessBoard.getPieces(), room.getTurn());
    }

    @Transactional(rollbackFor = Exception.class)
    public BoardDto move(long roomId, MoveDto moveDto) {
        Room room = roomDao.findById(roomId)
            .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessGame chessGame = ChessGame.of(loadChessBoard(room.getId()), room.getTurn());
        chessGame.move(moveDto.getFrom(), moveDto.getTo());
        roomDao.update(room.getId(), chessGame.getTurn());
        updateMovement(room.getId(), moveDto);

        return BoardDto.of(chessGame.getBoard(), chessGame.getTurn());
    }

    private void updateMovement(long roomId, MoveDto moveDto) {
        String fromPiece = squareDao.findByRoomIdAndPosition(roomId, moveDto.getFrom())
            .orElseThrow(() -> new NoSuchElementException(NO_SQUARE_MESSAGE))
            .getPiece();
        squareDao.update(roomId, moveDto.getFrom(), EMPTY.getName());
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

    public Status status(long roomId) {
        Room room = roomDao.findById(roomId)
            .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessBoard chessBoard = loadChessBoard(room.getId());
        ChessGame chessGame = ChessGame.of(chessBoard, room.getTurn());

        return chessGame.status();
    }

    public Room createRoom(String name, String password) {
        Optional<Room> room = roomDao.findByName(name);
        if (room.isPresent()) {
            throw new IllegalArgumentException(DUPLICATED_ROOM_NAME_MESSAGE);
        }
        Room newRoom = new Room(name, password);
        roomDao.save(newRoom);
        return roomDao.findByNameAndPassword(name, password).get();
    }

    public List<RoomDto> findAllRooms() {
        return roomDao.findAll().stream()
            .map(RoomDto::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(long roomId, String password) {
        Optional<Room> optionalRoom = roomDao.findByIdAndPassword(roomId, password);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException(INVALID_PASSWORD_MESSAGE);
        }
        validateDeletableState(optionalRoom.get());

        squareDao.removeAll(roomId);
        roomDao.delete(roomId);
    }

    private void validateDeletableState(Room room) {
        if (!room.isDeletable()) {
            throw new IllegalStateException(DELETE_NOT_ALLOWED_WHEN_RUNNING_MESSAGE);
        }
    }
}
