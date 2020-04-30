package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.entity.RoomEntity;

@Repository
public interface RoomRepository extends CrudRepository<RoomEntity, Long> {
}
