package wooteco.chess.db;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessPieceRepository extends CrudRepository<ChessPiece, String> {

    @Query("SELECT COUNT(*) FROM board_status WHERE game_id = :game_id")
    int countSavedPieces(@Param("game_id") String gameId);

    @Modifying
    @Query("INSERT INTO board_status (game_id, position, piece) VALUES (:gameId, :position, :piece)")
    void savePiece(String gameId, String position, String piece);

    @Modifying
    @Query("UPDATE board_status SET piece = :piece WHERE game_id = :game_id AND position = :position")
    void updatePiece(@Param("game_id") String gameId, @Param("position") String position, @Param("piece") String piece);

    @Override
    void deleteById(String gameId);

    @Query("SELECT * FROM board_status WHERE game_id = :gameId")
    List<ChessPiece> findByGameId(String gameId);

    @Query("SELECT game_id FROM board_status GROUP BY game_id")
    List<String> findRooms();
}
