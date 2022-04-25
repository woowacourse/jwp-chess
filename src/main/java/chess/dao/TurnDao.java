package chess.dao;

import chess.domain.piece.Team;

public interface TurnDao {

    void init(String data);

    void update(String nowTurn, String nextTurn);

    String getTurn();

    void reset(Team data);
}
