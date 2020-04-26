package wooteco.chess.repository;

import chess.dto.BoardDto;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wooteco.chess.entity.ChessBoard;

@Repository
public interface BoardRepository extends CrudRepository<ChessBoard, Long> {
    @Query("INSERT INTO chessBoard VALUES (:position, :pieceName)")
    void insert(@Param("position") String position, @Param("pieceName") String pieceName);

    @Query("SELECT * FROM chessBoard LIMIT 1")
    BoardDto findFirst();

//    @Query("DELETE FROM board")
//    void deleteBoard();
}
