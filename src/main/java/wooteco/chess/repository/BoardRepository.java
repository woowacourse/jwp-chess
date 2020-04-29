package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wooteco.chess.entity.PieceEntity;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<PieceEntity, Long> {
    @Modifying
    @Query("INSERT INTO board(position, pieceName) VALUES (:position, :pieceName)")
    void insert(@Param("position") String position, @Param("pieceName") String pieceName);

    @Override
    List<PieceEntity> findAll();
}
