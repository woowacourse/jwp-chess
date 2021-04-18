package chess.controller;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Running;
import chess.domain.piece.Piece;
import chess.repository.room.Room;
import chess.repository.room.RoomDao;
import chess.util.JsonConverter;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.List;

public class SparkChessController {
    private final RoomDao roomDAO = new RoomDao();
    private ChessGame chessGame;

    public Response createRoom(String name) {
        try {
            roomDAO.validateRoomExistence(name);
            initializeChessBoard();
            Response response = new Response(chessGame, StatusCode.SUCCESSFUL);
            response.add("name", name);
            return response;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            Response response = new Response(StatusCode.CONFLICT);
            response.add("alert", name + "는 이미 존재하는 방입니다.");
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Response(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Response movePiece(List<String> input) {
        try {
            chessGame.play(input);
            return new Response(chessGame, StatusCode.SUCCESSFUL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Response(StatusCode.BAD_REQUEST);
        }
    }

    private void initializeChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        chessGame = new ChessGame(chessBoard, Color.WHITE, new Ready());
        chessGame.start(Collections.singletonList("start"));
    }

    public Response saveRoom(String request) {
        try {
            Room room = createRoomToSave(request);
            roomDAO.addRoom(room);
            return new Response(StatusCode.SUCCESSFUL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Response(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Room createRoomToSave(String request) {
        JsonObject roomJson = JsonConverter.fromJson(request);
        String name = roomJson.get("name").getAsString();
        String turn = roomJson.get("turn").getAsString();
        JsonObject state = roomJson.get("state").getAsJsonObject();
        return new Room(name, turn, state);
    }

    public Response loadRoom(String name) {
        try {
            Room room = roomDAO.findByRoomId(name);
            setChessGame(room);
            Response response = new Response(chessGame, StatusCode.SUCCESSFUL);
            response.add("name", name);
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Response(StatusCode.INTERNAL_SERVER_ERROR);
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
        JsonObject pieceJson = JsonConverter.fromJson(stateJson.get(position));
        String type = pieceJson.get("type").getAsString();
        String color = pieceJson.get("color").getAsString();

        Type pieceType = Type.convert(type);
        return pieceType.createPiece(Position.of(position), Color.convert(color));
    }

    public Response getAllSavedRooms() {
        try {
            Response response = new Response(StatusCode.SUCCESSFUL);
            response.add("roomNames", roomDAO.getAllRoom());
            return response;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Response(StatusCode.BAD_GATEWAY);
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
