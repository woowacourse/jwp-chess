package chess.service;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Running;
import chess.domain.piece.Piece;
import chess.dto.MoveRequestDto;
import chess.repository.room.Room;
import chess.repository.room.SpringRoomDao;
import chess.util.JsonConverter;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpringChessService {
    private static final String SPACE = " ";
    private static final String START = "start";

    private final SpringRoomDao roomDao;

    public SpringChessService(SpringRoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public long initializeRoom(String roomName) {
        roomDao.validateRoomExistence(roomName);
        ChessGame chessGame = initializeChessBoard();
        Room room = createRoom(roomName, chessGame);

        return roomDao.saveRoom(room);
    }

    private ChessGame initializeChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        Ready gameState = new Ready();

        ChessGame chessGame = new ChessGame(chessBoard, Color.WHITE, gameState);
        chessGame.start(Collections.singletonList(START));

        return chessGame;
    }

    public ChessGame movePiece(MoveRequestDto moveRequestDto) {
        long id = moveRequestDto.getId();
        Room room = roomDao.findById(id);

        ChessGame chessGame = createChessGame(room);
        List<String> command = Arrays.asList(moveRequestDto.getCommand().split(SPACE));
        chessGame.play(command);

        room = createRoom(room.getName(), chessGame);
        roomDao.updateRoom(room);

        return chessGame;
    }

    public ChessGame loadRoom(long id) {
        Room room = roomDao.findById(id);

        return createChessGame(room);
    }

    private Room createRoom(String roomName, ChessGame chessGame) {
        String turn = chessGame.getTurnAsString();
        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        JsonObject state = JsonConverter.toJsonObject(chessBoard);

        return new Room(roomName, turn, state);
    }

    private ChessGame createChessGame(Room room) {
        ChessBoard chessBoard = new ChessBoard();
        Color turn = Color.convert(room.getTurn());
        JsonObject stateJson = room.getState();
        for (String position : stateJson.keySet()) {
            Piece piece = getPiece(stateJson, position);
            chessBoard.replace(Position.of(position), piece);
        }
        return new ChessGame(chessBoard, turn, new Running());
    }

    private Piece getPiece(JsonObject stateJson, String position) {
        JsonObject pieceJson = JsonConverter.fromJson(stateJson.get(position));
        String type = pieceJson.get("type").getAsString();
        String color = pieceJson.get("color").getAsString();

        Type pieceType = Type.convert(type);

        return pieceType.createPiece(Position.of(position), Color.convert(color));
    }

    public List<String> getAllSavedRooms() {
        try {
            return roomDao.getAllRoom();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public void deleteRoom(String roomName) {
        roomDao.deleteRoom(roomName);
    }

    public long getRoomId(String roomName) {
        return roomDao.getRoomIdByName(roomName);
    }
}
