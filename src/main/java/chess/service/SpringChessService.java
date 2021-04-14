package chess.service;

import chess.controller.Response;
import chess.controller.StatusCode;
import chess.database.room.Room;
import chess.database.room.RoomDAO;
import chess.database.room.SpringRoomDAO;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Running;
import chess.domain.piece.Piece;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SpringChessService {
    public static final Gson gson = new Gson();
    private ChessGame chessGame;
    private final SpringRoomDAO roomDAO;

    public SpringChessService(SpringRoomDAO roomDAO) {
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

    public Optional<ChessGame> movePiece(List<String> input) {
        try {
            chessGame.play(input);
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
        JsonObject roomJson = gson.fromJson(request, JsonObject.class);
        String name = roomJson.get("name").getAsString();
        String turn = roomJson.get("turn").getAsString();
        JsonObject state = roomJson.get("state").getAsJsonObject();
        return new Room(name, turn, state);
    }

//    public Response loadRoom(String request) {
//        try {
//            JsonObject roomJson = gson.fromJson(request, JsonObject.class);
//            String roomId = roomJson.get("room_id").getAsString();
//            Room room = roomDAO.findByRoomId(roomId);
//            setChessGame(room);
//            Response response = new Response(chessGame, StatusCode.SUCCESSFUL);
//            response.add("room_id", roomId);
//            return response;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return new Response(StatusCode.INTERNAL_SERVER_ERROR);
//        }
//    }

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
        JsonObject pieceJson = gson.fromJson(stateJson.get(position), JsonObject.class);
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

    public Response resetGameAsReadyState() {
        try {
            initializeChessBoard();
            return new Response(StatusCode.SUCCESSFUL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Response(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}
