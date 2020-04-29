package wooteco.chess.db;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.position.Position;

@Repository
public interface ChessPieceRepository extends CrudRepository<ChessPiece, String> {

    @Query("SELECT COUNT(*) FROM board_status WHERE game_id = :gameId")
    int countSavedPieces(@Param("game_id") String gameId);

    @Override
    <S extends ChessPiece> S save(S entity);

    @Override
    <S extends ChessPiece> Iterable<S> saveAll(Iterable<S> entities);

    @Query("SELECT piece FROM board_status WHERE game_id = :gameId AND position = :position")
    String findPieceNameByPosition(@Param("game_id") String gameId, @Param("position") Position position);

    @Query("UPDATE board_status SET piece = :piece WHERE game_id = :gameId AND position = :position")
    void updatePiece(@Param("game_id") String gameId, @Param("position") Position position, @Param("piece") Piece piece);

    @Override
    void deleteById(String gameId);
}
