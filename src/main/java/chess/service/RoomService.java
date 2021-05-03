package chess.service;

import chess.dao.RoomDAO;
import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.domain.Team;
import chess.dto.RoomDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public final class RoomService {

    private static final String BLACK = "black";

    private final RoomDAO roomDAO;
    private final Rooms rooms;

    public RoomService(final RoomDAO roomDAO, final Rooms rooms) {
        this.roomDAO = roomDAO;
        this.rooms = rooms;
    }

    public List<RoomDTO> allRooms() {
        return roomDAO.allRooms();
    }

    public void createRoom(final String title, final int userId) {
        roomDAO.createRoom(title, userId);
    }

    public String createdRoomId() {
        return roomDAO.createdRoomId();
    }

    public void loadAllRooms() {
        for (String roomId : roomDAO.allRoomIds()) {
            rooms.addRoom(roomId, new ChessGame());
        }
    }

    public void enrollUserByColor(final String roomId, final int userId, final String color) {
        if (BLACK.equals(color)) {
            roomDAO.enrollBlackUser(roomId, userId);
            return;
        }
        roomDAO.enrollWhiteUser(roomId, userId);
    }

    public void changeStatus(final String roomId) {
        roomDAO.changeStatusEndByRoomId(roomId);
    }

    public void addRoom(final String roomId, final ChessGame chessGame) {
        rooms.addRoom(roomId, chessGame);
    }

    public void move(final String roomId, final String startPoint, final String endPoint) {
        ChessGame chessGame = loadGameByRoomId(roomId);
        chessGame.move(startPoint, endPoint);
    }

    public ChessGame loadGameByRoomId(final String roomId) {
        return rooms.loadGameByRoomId(roomId);
    }

    public boolean checkRightTurn(final String roomId, final String clickedSection,
        final String password) {
        ChessGame chessGame = loadGameByRoomId(roomId);

        if (!chessGame.checkRightTurn(clickedSection)) {
            return false;
        }

        Team turn = chessGame.turn();
        if (Team.BLACK.equals(turn)) {
            return password.equals(roomDAO.findBlackUserPassword(roomId));
        }
        return password.equals(roomDAO.findWhiteUserPassword(roomId));
    }
}
