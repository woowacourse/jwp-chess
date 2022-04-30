package chess.model.room;

import chess.entity.GameEntity;

public class Room {
    private final String name;
    private final Password password;

    private Room(String name, Password password) {
        this.name = name;
        this.password = password;
    }

    public static Room fromHashedPassword(String name, String hashedPassword) {
        return new Room(name, Password.ofHashedText(hashedPassword));
    }

    public static Room fromPlainPassword(String name, String plainPassword) {
        return new Room(name, Password.ofPlainText(plainPassword));
    }

    public boolean isSamePassword(String plainPassword) {
        return password.isSamePassword(plainPassword);
    }
}
