package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChessRoomRepository extends CrudRepository<ChessRoom, Long> {
    @Override
    List<ChessRoom> findAll();
}
