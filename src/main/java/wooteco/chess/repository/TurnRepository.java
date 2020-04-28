package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.TurnEntity;

/**
 *    Turn Repository 인터페이스 입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
public interface TurnRepository extends CrudRepository<TurnEntity, Long> {
	@Override
	List<TurnEntity> findAll();
}
