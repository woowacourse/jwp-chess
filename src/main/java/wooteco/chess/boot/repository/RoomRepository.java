package wooteco.chess.boot.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.boot.entity.RoomEntity;

public interface RoomRepository extends CrudRepository<RoomEntity, Long> {

}
