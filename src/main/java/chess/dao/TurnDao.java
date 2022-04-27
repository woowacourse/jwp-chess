package chess.dao;

import chess.dto.TurnDto;

public interface TurnDao {

    TurnDto findTurn(final int roomId);

    void updateTurn(final int roomId, final String turn);

    void initializeTurn(final int roomId);
}
