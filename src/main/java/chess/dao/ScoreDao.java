package chess.dao;

import chess.controller.web.dto.score.ScoreResponseDto;
import chess.domain.manager.GameStatus;

public interface ScoreDao {
    Long saveScore(GameStatus gameStatus, Long gameId);

    Long updateScore(GameStatus gameStatus, Long gameId);

    ScoreResponseDto findScoreByGameId(Long gameId);
}
