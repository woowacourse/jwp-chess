package wooteco.chess.dao.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {
}
