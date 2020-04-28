package wooteco.chess.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.dto.PlayerDto;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerDto, Long> {

}
