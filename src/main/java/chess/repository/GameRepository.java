package chess.repository;

import chess.dao.GameDao;
import chess.domain.piece.Color;
import chess.entity.GameEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final GameDao gameDao;

    public GameRepository(@Qualifier("GameDbDao") GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public Color getColorFromStorage(int gameId) {
        return Color.from(gameDao.findById(GameEntity.of(gameId)).getTurn());
    }

    public int saveGameGetKey(String turn) {
        return gameDao.insertWithKeyHolder(GameEntity.of(turn));
    }

    public void update(int gameId, String turn) {
        gameDao.updateById(GameEntity.of(gameId, turn));
    }

    public void deleteGame(int gameId) {
        gameDao.deleteById(GameEntity.of(gameId));
    }
}
