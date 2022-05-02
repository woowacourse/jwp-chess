package chess.dto;

import java.util.Objects;

public class RoomDto {

    private final long id;
    private final String name;
    private final String password;

    public RoomDto(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return id == roomDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
