package chess.dto;

import java.util.Objects;

public class RoomDto {

    private long id;
    private String name;
    private String password;

    public RoomDto() {
    }

    public RoomDto(String name, String password) {
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
