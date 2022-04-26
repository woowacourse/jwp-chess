package chess.dao;

import chess.dto.TurnDto;

public interface TurnDao {

    TurnDto findTurn(final long roomId);

    void updateTurn(final long roomId, final String turn);

    void resetTurn(final long roomId);
}
