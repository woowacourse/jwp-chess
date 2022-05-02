package chess.dao;

import chess.dao.entity.PieceEntity;
import chess.domain.position.Position;

import java.util.List;

public interface PieceDao {

    void removeByPosition(Long gameId, Position position);

    void removeAll();

    void save(PieceEntity piece);

    void saveAll(List<PieceEntity> pieces);

    List<PieceEntity> findPiecesByGameId(Long gameId);

    void updatePosition(Long gameId, Position position, Position updatedPosition);
}
