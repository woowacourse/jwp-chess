package chess.dao;

import chess.dao.dto.history.HistoryDto;

import java.util.List;

public interface HistoryDao {

    Long save(final HistoryDto historyDto);

    List<HistoryDto> findByGameId(final Long gameId);
}
