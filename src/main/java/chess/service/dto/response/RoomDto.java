package chess.service.dto.response;

import chess.repository.dao.entity.GameEntity;

public class RoomDto {
    private final Integer id;
    private final String name;
    private final String status;

    public RoomDto(GameEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.status = entity.getStatus();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
