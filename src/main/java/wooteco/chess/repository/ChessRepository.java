package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.dto.Commands;

import java.util.List;

public interface ChessRepository extends CrudRepository<Commands, Long> {
    @Override
    List<Commands> findAll();
}
