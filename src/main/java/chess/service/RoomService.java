package chess.service;

import chess.dao.RoomDAO;
import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.dto.RoomDTO;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class RoomService {

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

    public void enrollBlackUser(final String roomId, final int blackUserId) {
        roomDAO.enrollBlackUser(roomId, blackUserId);
    }

    public void enrollWhiteUser(final String roomId, final int white) {
        roomDAO.enrollWhiteUser(roomId, white);
    }

    public void changeStatus(final String roomId) {
        roomDAO.changeStatusEndByRoomId(roomId);
    }

    public List<String> allRoomsId() {
        return roomDAO.allRoomIds();
    }

    public void addRoom(final String roomId, final ChessGame chessGame) {
        rooms.addRoom(roomId, chessGame);
    }

    public ChessGame loadGameByRoomId(final String roomId) {
        return rooms.loadGameByRoomId(roomId);
    }
}
