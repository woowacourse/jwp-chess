package chess.dao;

import chess.controller.web.dto.state.StateResponseDto;
import chess.domain.manager.ChessManager;

public interface StateDao {
    Long saveState(final ChessManager chessManager, final Long gameId);

    Long updateState(final ChessManager chessManager, final Long gameId);

    StateResponseDto findStateByGameId(final Long gameId);
}
