package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wooteco.chess.dto.Commands;

import java.util.List;

@Repository
public interface CommandsRepository extends CrudRepository<Commands, Long> {
    @Override
    List<Commands> findAll();
}
