package chess.dao;

import chess.dao.dto.score.ScoreDto;
import chess.domain.entity.Score;
import chess.domain.manager.GameStatus;

public interface ScoreDao {
    Long save(final ScoreDto stateDto);

    Long update(final ScoreDto scoreDto);

    ScoreDto findByGameId(final Long gameId);

    ScoreDto findById(Long id);
}
