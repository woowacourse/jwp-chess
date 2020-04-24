package wooteco.chess.dao;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.dto.Commands;

import java.util.List;

public interface ChessDao extends CrudRepository<Commands, Long> {
    @Override
    List<Commands> findAll();

    @Override
    void deleteAll();

    @Override
    <S extends Commands> S save(S entity);
}