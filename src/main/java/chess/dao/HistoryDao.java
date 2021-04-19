package chess.dao;

import chess.dao.dto.history.HistoryDto;
import chess.domain.history.History;

import java.util.List;

public interface HistoryDao {

    Long saveHistory(final History history, final Long gameId);

    List<HistoryDto> findHistoryByGameId(final Long gameId);
}
