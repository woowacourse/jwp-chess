package chess.dao;

import chess.entity.PieceEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

public interface PieceDao {

    void insert(PieceEntity pieceEntity);
    PieceEntity find(PieceEntity pieceEntity);
    List<PieceEntity> findAll(PieceEntity pieceEntity);
    void update(PieceEntity from, PieceEntity to);
    void delete(PieceEntity pieceEntity);
    void deleteAll(PieceEntity pieceEntity);

}
