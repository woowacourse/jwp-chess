package chess.dto.request;

import chess.entity.RoomEntity;

public class RoomRequestDto {
    private final String name;
    private final String password;

    public RoomRequestDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public RoomEntity toEntity() {
        return new RoomEntity(null, null, name, null, null);
    }
}
