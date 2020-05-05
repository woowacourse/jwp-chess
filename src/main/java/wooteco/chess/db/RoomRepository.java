package wooteco.chess.db;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {

    @Override
    List<Room> findAll();
}
