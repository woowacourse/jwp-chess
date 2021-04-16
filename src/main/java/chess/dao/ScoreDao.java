package chess.dao;

import chess.controller.web.dto.score.ScoreResponseDto;
import chess.domain.manager.GameStatus;

public interface ScoreDao {
    Long saveScore(final GameStatus gameStatus, final Long gameId);

    Long updateScore(final GameStatus gameStatus, final Long gameId);

    ScoreResponseDto findScoreByGameId(final Long gameId);
}
