package chess.domain.repository.history;

import chess.domain.history.History;

import java.util.List;

public interface HistoryRepository {
    Long save(History history);

    List<History> findByGameId(Long gameId);
}
