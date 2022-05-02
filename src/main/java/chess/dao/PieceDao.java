package chess.dao;

import chess.dao.entity.PieceEntity;

import java.util.List;

public interface PieceDao {

    void removeByPosition(Long gameId, String position);

    void removeAll();

    void save(PieceEntity piece);

    void saveAll(List<PieceEntity> pieces);

    List<PieceEntity> findPiecesByGameId(Long gameId);

    void updatePosition(Long gameId, String position, String updatedPosition);
}
