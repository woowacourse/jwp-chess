package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
	@Override
	List<Room> findAll();
}
