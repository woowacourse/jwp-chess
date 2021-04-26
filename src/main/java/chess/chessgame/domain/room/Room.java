package chess.chessgame.domain.room;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.user.GameUsers;
import chess.chessgame.domain.room.user.User;

import java.util.List;

import static chess.chessgame.domain.room.game.board.piece.attribute.Color.BLACK;
import static chess.chessgame.domain.room.game.board.piece.attribute.Color.WHITE;

public class Room {
    private final long roomId;
    private final String roomName;
    private final ChessGameManager gameManager;
    private final GameUsers users;

    public Room(long roomId, String roomName, ChessGameManager gameManager, List<User> users) {
        this(roomId, roomName, gameManager, new GameUsers(users));
    }

    public Room(long roomId, String roomName, ChessGameManager gameManager, GameUsers users) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.gameManager = gameManager;
        this.users = users;
    }

    public void enterUser(User user) {
        this.users.enterUser(user);
    }

    public boolean isMaxUser(){
        return users.isMaxUser();
    }

    public ChessGameManager getGameManager() {
        return gameManager;
    }

    public long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public User getWhiteUser() {
        return users.findUserByColor(WHITE);
    }

    public User getBlackUser() {
        return users.findUserByColor(BLACK);
    }

    public boolean isSamePassword(Color color, String password) {
        return users.isSamePassword(color, password);
    }
}
