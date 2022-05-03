package chess.dao;

import chess.domain.GameStatus;

public interface GameStatusDao {

    void create(GameStatus data, int roomId);

    void update(String nowStatus, String nextStatus, int roomId);

    String getStatus(int roomId);

    void reset(GameStatus data, int roomId);
}
