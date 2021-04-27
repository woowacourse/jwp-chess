package chess.service;

import chess.dao.PlayerDAO;
import chess.dao.RoomDAO;
import chess.domain.ChessGame;
import chess.domain.Rooms;
import chess.domain.Team;
import chess.domain.entity.Player;
import chess.dto.player.PlayerDTO;
import chess.dto.room.RoomDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class RoomService {
    private final Rooms rooms;
    private final RoomDAO roomDAO;
    private final PlayerDAO playerDAO;

    public RoomService(Rooms rooms, RoomDAO roomDAO, PlayerDAO playerDAO) {
        this.rooms = rooms;
        this.roomDAO = roomDAO;
        this.playerDAO = playerDAO;
    }

    public List<RoomDTO> allRooms() {
        return roomDAO.allRooms().stream()
                .map(room -> {
                    String blackUser = playerDAO.findNicknameById(room.getBlackUserId());
                    String whiteUser = playerDAO.findNicknameById(room.getWhiteUserId());
                    int status = room.getStatus();
                    return new RoomDTO(
                            room.getId(), room.getTitle(), blackUser, whiteUser, status, (status == 1 || status == 2)
                    );
                }).collect(Collectors.toList());
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

    public boolean checkRightTurn(final String roomId, final PlayerDTO user, final String clickedSection) {
        ChessGame chessGame = loadChessGameById(roomId);

        if (user.getId() == -1 || !chessGame.checkRightTurn(clickedSection)) {
            return false;
        }

        Team turn = chessGame.turn();
        if (Team.BLACK.equals(turn)) {
            return roomDAO.findBlackUserById(roomId)
                    .equals(Integer.toString(user.getId()));
        }
        return roomDAO.findWhiteUserById(roomId)
                .equals(Integer.toString(user.getId()));
    }

    public PlayerDTO participatedUser(final String id, final String color) {
        String userId = roomDAO.findUserIdByRoomIdAndColor(id, color);
        Player player = playerDAO.findById(userId)
                .orElse(new Player(-1));
        return new PlayerDTO(player);
    }
}
