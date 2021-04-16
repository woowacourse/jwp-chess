package chess.web.dao.game;

import chess.web.dao.entity.ChessGameEntity;
import chess.web.dao.entity.GameStatusEntity;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ChessGameRepository {
    ChessGameEntity save(ChessGameEntity chessRoomEntity);

    ChessGameEntity findById(Long id);

    List<ChessGameEntity> findAll();

    GameStatusEntity findStatusByGameId(Long gameId);

    ChessGameEntity updateCurrentTurnTeamColor(ChessGameEntity chessGameEntity);

    void remove(Long gameId);

    void removeAll();
}
