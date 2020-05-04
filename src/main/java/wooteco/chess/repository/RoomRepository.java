package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.RoomEntity;

/**
 *    RoomRepository 인터페이스입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
public interface RoomRepository extends CrudRepository<RoomEntity, Long> {

}
