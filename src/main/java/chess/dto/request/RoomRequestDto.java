package chess.dto.request;

import chess.entity.RoomEntity;

public class RoomRequestDto {
    private String name;

    private RoomRequestDto() {
    }

    public RoomRequestDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RoomEntity toEntity() {
        return new RoomEntity(null, name, null, null);
    }
}
