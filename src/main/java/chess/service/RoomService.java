package chess.service;

import chess.dao.RoomDAO;
import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.dto.room.RoomDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class RoomService {
    private final Rooms rooms;
    private final RoomDAO roomDAO;

    public RoomService(Rooms rooms, RoomDAO roomDAO) {
        this.rooms = rooms;
        this.roomDAO = roomDAO;
    }

    public List<RoomDTO> allRooms() {
        return roomDAO.allRooms();
    }

    public Long createRoom(final String name) {
        return roomDAO.createRoom(name);
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
}
