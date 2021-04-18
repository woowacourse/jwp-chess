package chess.service;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.feature.Type;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.Running;
import chess.domain.piece.Piece;
import chess.repository.room.Room;
import chess.repository.room.SpringRoomDao;
import chess.util.JsonConverter;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//TODO: chessGame 필드에서 없애고 db에서 매번 가져오는 식으로 리팩토링 (매 수 둘 때마다 항상 저장해야함)
@Service
public class SpringChessService {
    private static final String SPACE = " ";
    private static final String START = "start";

    private final SpringRoomDao roomDAO;
    private ChessGame chessGame;

    public SpringChessService(SpringRoomDao roomDAO) {
        this.roomDAO = roomDAO;
    }

    public ChessGame createRoom(String roomName) {
        roomDAO.validateRoomExistence(roomName);
        initializeChessBoard();
        return chessGame;
    }

    public ChessGame movePiece(String command) {
        List<String> commands = Arrays.asList(command.split(SPACE));
        chessGame.play(commands);
        return chessGame;
    }

    private void initializeChessBoard() {
        ChessBoard chessBoard = new ChessBoard();
        chessGame = new ChessGame(chessBoard, Color.WHITE, new Ready());
        chessGame.start(Collections.singletonList(START));
    }

    public boolean saveRoom(Room room) {
        try {
            roomDAO.addRoom(room);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ChessGame loadRoom(String name) {
        Room room = roomDAO.findByRoomName(name);
        createChessGame(room);
        return chessGame;
    }

    private void createChessGame(Room room) {
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
