package wooteco.chess.dao.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.domain.Room;

public interface RoomRepository extends CrudRepository<Room, Long> {
}
