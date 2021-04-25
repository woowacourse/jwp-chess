package chess.service;

import chess.dao.RoomDAO;
import chess.dao.UserDAO;
import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.dto.room.RoomDTO;
import chess.dto.user.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public final class RoomService {
    private final Rooms rooms;
    private final RoomDAO roomDAO;
    private final UserDAO userDAO;

    public RoomService(Rooms rooms, RoomDAO roomDAO, UserDAO userDAO) {
        this.rooms = rooms;
        this.roomDAO = roomDAO;
        this.userDAO = userDAO;
    }

    public List<RoomDTO> allRooms() {
        return roomDAO.allRooms();
    }

    public Long createRoom(final String name, final int whiteUserId) {
        return roomDAO.createRoom(name, whiteUserId);
    }

    public void changeStatus(final String roomId) {
        roomDAO.changeStatusEndByRoomId(roomId);
    }

    public void loadRooms() {
        roomDAO.allRoomIds().forEach(id -> rooms.addRoom(id, new ChessGame()));
    }

    public void addNewRoom(final String id) {
        ChessGame chessGame = new ChessGame();
        chessGame.initialize();
        rooms.addRoom(id, chessGame);
    }

    public void addNewRoom(final Long id) {
        ChessGame chessGame = new ChessGame();
        chessGame.initialize();
        rooms.addRoom(Long.toString(id), chessGame);
    }

    public ChessGame initializeChessGame(final String id) {
        ChessGame chessGame = rooms.loadGameByRoomId(id);
        chessGame.initialize();
        return chessGame;
    }

    public ChessGame loadChessGameById(final String id) {
        return rooms.loadGameByRoomId(id);
    }

    public ChessGame movePiece(final String id, final String startPoint, final String endPoint) {
        ChessGame chessGame = loadChessGameById(id);
        chessGame.move(startPoint, endPoint);
        return chessGame;
    }

    public void joinBlackUser(final String roomId, final int blackUserId) {
        roomDAO.joinBlackUser(roomId, blackUserId);
    }

    public Optional<UserDTO> findBlackUserById(final String id) {
        String blackUserId = roomDAO.findBlackUserById(id);
        return userDAO.findById(blackUserId);
    }

    public Optional<UserDTO> findWhiteUserById(final String id) {
        String whiteUserId = roomDAO.findWhiteUserById(id);
        return userDAO.findById(whiteUserId);
    }
}
