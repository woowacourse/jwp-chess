package chess.repository;

import chess.web.dto.RoomDto;
import java.util.List;
import java.util.Optional;

public interface RoomDaoInterface {
    int save(String name, String password);

    List<RoomDto> findAll();
    
    Optional<RoomDto> findById(int roomId);

    void delete(int roomId);
}
