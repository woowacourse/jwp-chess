package chess.web.dao.playerpieceposition;

import chess.web.dao.entity.GamePiecePositionEntity;
import chess.web.dao.entity.PiecePositionEntity;
import chess.web.domain.piece.Piece;
import chess.web.domain.position.PiecePosition;
import chess.web.domain.position.Position;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerPiecePositionRepository {
    void save(Long playerId, PiecePosition piecePosition) throws SQLException;

    Map<Position, Piece> findAllByGameId(Long gameId) throws SQLException;

    List<PiecePositionEntity> findAllByPlayerId(Long playerId) throws SQLException;

    GamePiecePositionEntity findGamePiecePositionByGameIdAndPositionId(Long gameId, Long positionId) throws SQLException;

    void updatePiecePosition(GamePiecePositionEntity gamePiecePositionEntity) throws SQLException;

    void removePiecePositionOfGame(GamePiecePositionEntity gamePiecePositionEntity) throws SQLException;

    void removeAllByPlayer(Long playerId) throws SQLException;

    void removeAll() throws SQLException;
}
