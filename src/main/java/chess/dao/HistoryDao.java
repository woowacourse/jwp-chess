package chess.dao;

import chess.controller.web.dto.history.HistoryResponseDto;
import chess.domain.history.History;

import java.util.List;

public interface HistoryDao {

    Long saveHistory(final History history, final Long gameId);

    List<HistoryResponseDto> findHistoryByGameId(final Long gameId);
}
