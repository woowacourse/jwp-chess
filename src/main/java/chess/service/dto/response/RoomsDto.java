package chess.service.dto.response;

import chess.repository.dao.entity.GameEntity;
import java.util.List;
import java.util.stream.Collectors;

public class RoomsDto {
    private final List<RoomDto> rooms;

    public RoomsDto(List<GameEntity> entities) {
        this.rooms = entities.stream()
                .map(RoomDto::new)
                .collect(Collectors.toList());
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }
}
