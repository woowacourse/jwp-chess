package wooteco.chess.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessPieceRepository extends CrudRepository<ChessPiece, Long> {

    @Query("SELECT COUNT(*) FROM board_status WHERE room = :roomId")
    int countSavedPiecesByRoomId(Long roomId);

    @Modifying
    @Query("UPDATE board_status SET piece = :piece WHERE room = :roomId AND position = :position")
    void updatePiece(Long roomId, String position, String piece);

    @Query("SELECT * FROM board_status WHERE room = :roomId")
    List<ChessPiece> findByRoomId(Long roomId);
}
