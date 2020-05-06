package wooteco.chess.boot.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import wooteco.chess.boot.entity.PieceEntity;

import java.util.List;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {

    @Override
    List<PieceEntity> findAll();

    @Query("SELECT * from piece where room_id = :roomId")
    List<PieceEntity> findAllByRoomId(Long roomId);

    @Modifying
    @Query("DELETE from piece where room_id = :roomId")
    void deleteAllByRoomId(Long roomId);
}
