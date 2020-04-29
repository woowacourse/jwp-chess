package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChessRoomRepository extends CrudRepository<ChessRoom, Long> {
    @Override
    <S extends ChessRoom> S save(S entity);

    @Override
    List<ChessRoom> findAll();
}
