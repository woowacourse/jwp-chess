package chess.repository;

import chess.web.dto.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    int save(RoomDto roomDto);

    Optional<RoomDto> findByName(String name);

    Optional<RoomDto> findById(int roomId);

    void deleteById(int id);

    List<RoomDto> findAll();
}
