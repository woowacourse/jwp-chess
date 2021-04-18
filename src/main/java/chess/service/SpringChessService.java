package chess.service;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Running;
import chess.domain.piece.Piece;
import chess.dto.MoveRequestDto;
import chess.repository.room.Room;
import chess.repository.room.SpringRoomDao;
import chess.util.JsonConverter;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class SpringChessService {
    private static final String SPACE = " ";

    private final SpringRoomDao roomDao;

    public SpringChessService(SpringRoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public ChessGame initializeRoom(String roomName) {
        roomDao.validateRoomExistence(roomName);
        ChessGame chessGame = initializeChessBoard();
        Room room = createRoom(roomName, chessGame);
        roomDao.addRoom(room);
        return chessGame;
    }

    private ChessGame initializeChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        Running gameState = new Running();
        return new ChessGame(chessBoard, Color.WHITE, gameState);
    }

    public ChessGame movePiece(MoveRequestDto moveRequestDto) {
        String roomName = moveRequestDto.getRoomName();
        Room room = roomDao.findByRoomName(roomName);

        ChessGame chessGame = createChessGame(room);
        List<String> commands = Arrays.asList(moveRequestDto.getCommand().split(SPACE));
        chessGame.play(commands);

        room = createRoom(roomName, chessGame);
        roomDao.addRoom(room);

        return chessGame;
    }

    public ChessGame loadRoom(String name) {
        Room room = roomDao.findByRoomName(name);
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
}
