package chess.dao;

import chess.dao.dto.state.StateDto;
import chess.domain.manager.ChessManager;

public interface StateDao {
    Long saveState(final ChessManager chessManager, final Long gameId);

    Long updateState(final ChessManager chessManager, final Long gameId);

    StateDto findStateByGameId(final Long gameId);
}
