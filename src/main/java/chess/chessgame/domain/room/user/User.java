package chess.chessgame.domain.room.user;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;

public class User {
    private final long userId;
    private final String password;
    private final Color color;

    public User(long userId, Color color, String password) {
        this.userId = userId;
        this.color = color;
        this.password = password;
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
}
