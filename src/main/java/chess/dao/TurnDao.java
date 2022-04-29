package chess.dao;

import chess.domain.piece.Team;

public interface TurnDao {

    void create(Team data, int roomId);

    void update(String nowTurn, String nextTurn, int roomId);

    String getTurn(int roomId);

    void reset(Team data, int roomId);
}
