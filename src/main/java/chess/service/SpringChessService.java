package chess.service;

import chess.database.room.Room;
import chess.database.room.SpringRoomDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Running;
import chess.domain.piece.Piece;
import chess.util.JsonConverter;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SpringChessService {
    private static final String SPACE = " ";

    private final SpringRoomDao roomDAO;
    private ChessGame chessGame;

    public SpringChessService(SpringRoomDao roomDAO) {
        this.roomDAO = roomDAO;
    }

    public Optional<ChessGame> createRoom(String roomId) {
        try {
            roomDAO.validateRoomExistence(roomId);
            initializeChessBoard();
            return Optional.of(chessGame);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<ChessGame> movePiece(String command) {
        try {
            List<String> commands = Arrays.asList(command.split(SPACE));
            chessGame.play(commands);
            return Optional.of(chessGame);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    private void initializeChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        chessGame = new ChessGame(chessBoard, Color.WHITE, new Ready());
        chessGame.start(Collections.singletonList("start"));
    }

    public boolean saveRoom(String request) {
        try {
            Room room = createRoomToSave(request);
            roomDAO.addRoom(room);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Room createRoomToSave(String request) {
        JsonObject roomJson = JsonConverter.toJsonObject(request);
        String name = roomJson.get("name").getAsString();
        String turn = roomJson.get("turn").getAsString();
        JsonObject state = roomJson.get("state").getAsJsonObject();
        return new Room(name, turn, state);
    }

    public Optional<ChessGame> loadRoom(String name) {
        try {
            Room room = roomDAO.findByRoomName(name);
            setChessGame(room);
            return Optional.of(chessGame);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    private void setChessGame(Room room) {
        ChessBoard chessBoard = new ChessBoard();
        Color turn = Color.convert(room.getTurn());
        JsonObject stateJson = room.getState();
        for (String position : stateJson.keySet()) {
            Piece piece = getPiece(stateJson, position);
            chessBoard.replace(Position.of(position), piece);
        }
        chessGame = new ChessGame(chessBoard, turn, new Running());
    }

    private Piece getPiece(JsonObject stateJson, String position) {
        JsonObject pieceJson = JsonConverter.toJsonObject(stateJson.get(position));
        String type = pieceJson.get("type").getAsString();
        String color = pieceJson.get("color").getAsString();

        Type pieceType = Type.convert(type);
        return pieceType.createPiece(Position.of(position), Color.convert(color));
    }

    public List<String> getAllSavedRooms() {
        try {
            return roomDAO.getAllRoom();
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
}
