package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MoveCommandRepository extends CrudRepository<MoveCommand, Long> {
    @Override
    <S extends MoveCommand> S save(S entity);

    @Override
    List<MoveCommand> findAll();
}
