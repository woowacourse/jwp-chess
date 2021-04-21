package chess.dao;

import chess.dao.dto.state.StateDto;
import chess.domain.manager.ChessManager;

public interface StateDao {
    Long save(final ChessManager chessManager, final Long gameId);

    Long update(final ChessManager chessManager, final Long gameId);

    StateDto findByGameId(final Long gameId);
}
