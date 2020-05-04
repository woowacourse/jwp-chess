package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<BoardEntity, Long> {

    @Override
    BoardEntity save(BoardEntity boardEntity);

    @Query("SELECT * FROM board WHERE roomId = :roomId AND position = :position")
    BoardEntity findByRoomIdAndPosition(@Param("roomId") Long roomId, @Param("position") String position);

    @Query("SELECT * FROM board where roomId = :roomId")
    List<BoardEntity> findByRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Query("DELETE FROM board WHERE roomId = :roomId")
    void deleteByRoomId(@Param("roomId") Long roomId);

}