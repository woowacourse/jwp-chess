package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.RoomEntity;

/**
 *    RoomRepository 인터페이스입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
public interface RoomRepository extends CrudRepository<RoomEntity, Long> {
	@Override
	List<RoomEntity> findAll();
}
