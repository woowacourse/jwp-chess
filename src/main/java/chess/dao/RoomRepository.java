package chess.dao;

import chess.web.dto.RoomDto;
import java.util.Optional;

public interface RoomRepository {
    int save(String name);

    Optional<RoomDto> find(String name);
}
