package chess.domain;

import exception.ChessException;
import exception.ExceptionStatus;
import java.util.Objects;

public class Room {
    private long id;
    private String name;
    private String pw;
    private long gameId;

    public Room() {}

    public Room(long id, String name, String pw, long gameId) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.gameId = gameId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPw() {
        return pw;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(final long gameId) {
        this.gameId = gameId;
    }

    public void checkPassword(Room room) {
        if (!room.getPw().equals(pw)) {
            throw new ChessException(ExceptionStatus.LOGIN_FAIL);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id && gameId == room.gameId && Objects.equals(name, room.name)
            && Objects.equals(pw, room.pw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pw, gameId);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                ", gameId='" + gameId + '\'' +
                '}';
    }
}
