package web.dto;

import java.util.Objects;

public class RoomDto {

    private final int id;
    private final int chessGameId;
    private final String name;
    private final String password;

    public RoomDto(int id, int chessGameId, String name, String password) {
        this.id = id;
        this.chessGameId = chessGameId;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public int getChessGameId() {
        return chessGameId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomDto roomDto = (RoomDto) o;
        return id == roomDto.id && chessGameId == roomDto.chessGameId && Objects.equals(name, roomDto.name)
                && Objects.equals(password, roomDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chessGameId, name, password);
    }
}
