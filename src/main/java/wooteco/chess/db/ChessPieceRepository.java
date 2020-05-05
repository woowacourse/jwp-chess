package wooteco.chess.db;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessPieceRepository extends CrudRepository<ChessPiece, String> {
    @Query("SELECT COUNT(*) FROM board_status WHERE game_id = :game_id")
    int countSavedPieces(@Param("game_id") String gameId);

    @Modifying
    @Query("INSERT INTO board_status (game_id, position, piece) VALUES (:game_id, :position, :piece)")
    void savePiece(@Param("game_id") String gameId, @Param("position") String position, @Param("piece") String piece);

    @Query("SELECT piece FROM board_status WHERE game_id = :game_id AND position = :position")
    String findPieceNameByPosition(@Param("game_id") String gameId, @Param("position") String position);

    @Modifying
    @Query("UPDATE board_status SET piece = :piece WHERE game_id = :game_id AND position = :position")
    void updatePiece(@Param("game_id") String gameId, @Param("position") String position, @Param("piece") String piece);

    @Override
    void deleteById(String gameId);

    @Query("SELECT DISTINCT game_id FROM board_status")
    List<String> findRoomNames();
}
