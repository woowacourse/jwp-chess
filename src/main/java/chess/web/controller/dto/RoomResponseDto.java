package chess.web.controller.dto;

import chess.domain.entity.Room;

public class RoomResponseDto {
    private final String title;
    private final String password;
    private final Long id;

    public RoomResponseDto(Room room) {
        this.title = room.getTitle();
        this.password = room.getPassword();
        this.id = room.getId();
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
