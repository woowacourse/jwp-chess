package chess.dao;

import chess.dao.dto.state.StateDto;
import chess.domain.manager.ChessManager;

public interface StateDao {
    Long save(StateDto stateDto);

    Long update(final StateDto stateDto);

    StateDto findByGameId(final Long gameId);

    StateDto findById(Long id);
}
