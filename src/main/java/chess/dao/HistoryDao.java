package chess.dao;

import chess.dao.dto.history.HistoryDto;
import chess.domain.history.History;

import java.util.List;

public interface HistoryDao {

    Long save(final History history, final Long gameId);

    List<HistoryDto> findByGameId(final Long gameId);
}
