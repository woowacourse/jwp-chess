package chess.dao;

import chess.dao.dto.score.ScoreDto;
import chess.domain.manager.GameStatus;

public interface ScoreDao {
    Long save(final GameStatus gameStatus, final Long gameId);

    Long update(final GameStatus gameStatus, final Long gameId);

    ScoreDto findByGameId(final Long gameId);
}
