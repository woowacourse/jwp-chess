package chess.chessgame.domain.room.user;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;

public class User {
    private final long userId;
    private final Color color;
    private final String password;
    private long roomId;

    public User(long userId, Color color, String password) {
        this.userId = userId;
        this.color = color;
        this.password = password;
    }

    public User(long userId, Color color, String password, long roomId) {
        this.userId = userId;
        this.color = color;
        this.password = password;
        this.roomId = roomId;
    }

    public Color getColor() {
        return color;
    }

    public boolean isSameColor(Color that) {
        return this.color == that;
    }

    public long getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public boolean isSamePassword(String that) {
        return this.password.equals(that);
    }
}
