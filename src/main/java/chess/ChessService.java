package chess;

import chess.dao.RoomDao;
import chess.dao.SquareDao;
import chess.domain.ChessBoard;
import chess.domain.ChessGame;
import chess.domain.ChessRoom;
import chess.domain.Status;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.PasswordDto;
import chess.dto.RoomCreationDto;
import chess.dto.RoomDto;
import chess.entity.RoomEntity;
import chess.entity.SquareEntity;
import chess.utils.PieceFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ChessService {

    private static final String NO_ROOM_MESSAGE = "해당 ID와 일치하는 Room이 존재하지 않습니다.";
    private static final String NO_SQUARE_MESSAGE = "해당 방, 위치에 존재하는 Square가 없습니다.";
    private static final String NO_SQUARES_MESSAGE = "해당 ID에 체스게임이 초기화되지 않았습니다.";
    private static final String NOT_END_GAME_MESSAGE = "게임이 종료되지 않았습니다.";

    private final RoomDao roomDao;
    private final SquareDao squareDao;

    public ChessService(RoomDao roomJdbcDao, SquareDao squareJdbcDao) {
        this.roomDao = roomJdbcDao;
        this.squareDao = squareJdbcDao;
    }

    public BoardDto startNewGame(long roomId) {
        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));

        ChessGame chessGame = new ChessGame();
        chessGame.start();
        squareDao.removeAll(room.getId());

        Map<Position, Piece> board = chessGame.getBoard();
        List<SquareEntity> squares = convertBoardToSquares(board);
        squareDao.saveAll(squares, room.getId());

        roomDao.updateTurn(room.getId(), chessGame.getTurn());
        return BoardDto.of(board, chessGame.getTurn());
    }

    public void end(long roomId) {
        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        roomDao.updateTurn(room.getId(), "empty");
        squareDao.removeAll(roomId);
    }

    public BoardDto load(long roomId) {
        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessBoard chessBoard = loadChessBoard(room.getId());

        return BoardDto.of(chessBoard.getPieces(), room.getTurn());
    }

    public BoardDto move(long roomId, MoveDto moveDto) {
        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessGame chessGame = ChessGame.of(loadChessBoard(room.getId()), room.getTurn());
        chessGame.move(moveDto.getFrom(), moveDto.getTo());
        roomDao.updateTurn(room.getId(), chessGame.getTurn());
        updateMovement(room.getId(), moveDto);

        return BoardDto.of(chessGame.getBoard(), chessGame.getTurn());
    }

    private void updateMovement(long roomId, MoveDto moveDto) {
        String fromPiece = squareDao.findByRoomIdAndPosition(roomId, moveDto.getFrom())
                .orElseThrow(() -> new NoSuchElementException(NO_SQUARE_MESSAGE))
                .getPiece();
        squareDao.update(roomId, moveDto.getFrom(), "empty");
        squareDao.update(roomId, moveDto.getTo(), fromPiece);
    }

    private List<SquareEntity> convertBoardToSquares(Map<Position, Piece> board) {
        return board.keySet().stream()
                .map(position -> new SquareEntity(position.convertToString(), board.get(position).convertToString()))
                .collect(Collectors.toList());
    }

    private ChessBoard loadChessBoard(long roomId) {
        List<SquareEntity> squares = squareDao.findByRoomId(roomId);
        if (squares.isEmpty()) {
            throw new NoSuchElementException(NO_SQUARES_MESSAGE);
        }
        Map<Position, Piece> board = new HashMap<>();
        for (SquareEntity square : squares) {
            Position position = Position.of(square.getPosition());
            Piece piece = PieceFactory.convertToPiece(square.getPiece());
            board.put(position, piece);
        }
        return new ChessBoard(() -> board);
    }

    public Status status(long roomId) {
        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessBoard chessBoard = loadChessBoard(room.getId());
        ChessGame chessGame = ChessGame.of(chessBoard, room.getTurn());

        return chessGame.status();
    }

    public long createRoom(RoomCreationDto roomCreationDto) {
        RoomEntity newRoom = new RoomEntity(roomCreationDto.getName(), roomCreationDto.getPassword());
        return roomDao.save(newRoom);
    }

    public List<RoomDto> list() {
        List<RoomEntity> rooms = roomDao.findAll();
        return rooms.stream()
                .map(room -> new RoomDto(room.getId(), room.getName()))
                .collect(Collectors.toList());
    }

    public void delete(long roomId, PasswordDto passwordDto) {
        RoomEntity room = roomDao.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException(NO_ROOM_MESSAGE));
        ChessRoom chessRoom = new ChessRoom(room.getName(), room.getPassword(), room.getTurn());
        if (chessRoom.isEnd()) {
            squareDao.removeAll(roomId);
            roomDao.deleteRoom(roomId);
            return;
        }
        chessRoom.checkSamePassword(passwordDto.getPassword());
        throw new IllegalStateException(NOT_END_GAME_MESSAGE);
    }
}
