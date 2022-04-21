package chess.web.dao;

import chess.web.dto.RoomDto;
import java.util.Optional;

public interface RoomRepository {
    void save(String name);

    Optional<RoomDto> find(String name);
}
