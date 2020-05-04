package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.TurnEntity;

/**
 *    Turn Repository 인터페이스 입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
public interface TurnRepository extends CrudRepository<TurnEntity, Long> {
	@Modifying
	@Query("UPDATE turn SET is_white_turn = :isWhiteTurn WHERE room_entity = :roomId")
	void update(@Param("isWhiteTurn") Boolean isWhiteTurn, @Param("roomId") Long roomId);
}
