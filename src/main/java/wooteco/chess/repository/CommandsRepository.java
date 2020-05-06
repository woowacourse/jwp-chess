package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandsRepository extends CrudRepository<MoveCommand, Long> {
    @Override
    List<MoveCommand> findAll();
}
