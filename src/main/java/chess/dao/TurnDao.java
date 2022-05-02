package chess.dao;

import chess.dto.TurnDto;

public interface TurnDao {

    TurnDto findTurn(final int roomId);

    void changeTurn(final int roomId, final String nextTeam, final String currentTeam);

    void updateTurn(final int roomId, final String startTeam);

    void insertTurn(final int roomId, final String team);
}
