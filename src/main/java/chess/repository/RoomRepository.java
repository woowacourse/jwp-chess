package chess.repository;

import chess.dto.RoomContentDto;

import java.util.List;

public interface RoomRepository<T> {

    List<RoomContentDto> findAll();

    T save(T room, String password);

    T getById(int roomId);

    String getPasswordById(int roomId);
}
