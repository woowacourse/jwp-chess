package chess.repository;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.repository.entity.PieceEntity;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PieceDao {
    int save(int boardId, String target, PieceEntity pieceEntity);

    void saveAll(int boardId, Map<Position, Piece> pieces);

    Optional<PieceEntity> findOne(int boardId, String position);

    List<PieceEntity> findAll(int boardId);

    void updateOne(int boardId, String afterPosition, PieceEntity pieceEntity);

    void deleteOne(int boardId, String position);

}
