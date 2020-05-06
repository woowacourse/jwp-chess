package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ChessGameTableRepository extends CrudRepository<ChessGameTable, Long> {
    @Override
    List<ChessGameTable> findAll();
}
