package chess.web.controller.dto;

import chess.domain.entity.Room;

public class RoomResponseDto {
    private final String title;
    private final String password;
    private final boolean finish;
    private final Long id;

    public RoomResponseDto(Room room, boolean finish) {
        this.title = room.getTitle();
        this.password = room.getPassword();
        this.id = room.getId();
        this.finish = finish;
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

    public boolean isFinish() {
        return finish;
    }
}
