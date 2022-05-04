package chess.database.dao;

import java.util.List;

import chess.database.entity.PieceEntity;
import chess.database.entity.PointEntity;

public interface PieceDao {
    void saveBoard(List<PieceEntity> entities);

    List<PieceEntity> findBoardByGameId(Long gameId);

    void deletePiece(PointEntity pointEntity, Long gameId);

    void updatePiece(PointEntity sourceEntity, PointEntity destinationEntity, Long gameId);

}
