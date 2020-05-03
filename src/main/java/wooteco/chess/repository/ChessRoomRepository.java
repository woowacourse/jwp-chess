package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessRoomRepository extends CrudRepository<ChessRoom, Long> {
    @Override
    List<ChessRoom> findAll();
}
