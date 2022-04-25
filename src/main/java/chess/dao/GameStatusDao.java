package chess.dao;

import chess.domain.GameStatus;

public interface GameStatusDao {

    void init(GameStatus data);

    void update(String nowStatus, String nextStatus);

    String getStatus();

    void reset(GameStatus data);
}
