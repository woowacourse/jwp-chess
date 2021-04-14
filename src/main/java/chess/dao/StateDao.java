package chess.dao;

import chess.controller.web.dto.state.StateResponseDto;
import chess.domain.manager.ChessManager;

public interface StateDao {
    Long saveState(ChessManager chessManager, Long gameId);

    Long updateState(ChessManager chessManager, Long gameId);

    StateResponseDto findStateByGameId(Long gameId);
}
