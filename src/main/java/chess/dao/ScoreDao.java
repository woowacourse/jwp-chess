package chess.dao;

import chess.dao.dto.score.ScoreDto;
import chess.domain.manager.GameStatus;

public interface ScoreDao {
    Long saveScore(final GameStatus gameStatus, final Long gameId);

    Long updateScore(final GameStatus gameStatus, final Long gameId);

    ScoreDto findScoreByGameId(final Long gameId);
}
