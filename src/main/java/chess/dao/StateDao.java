package chess.dao;

import chess.model.board.Board;
import chess.model.state.State;

public interface StateDao {

    int insert(final String id, final State state);

    State find(final String id, final Board board);

    int deleteFrom(final String id);

    void update(final String id, final State nowState, final State nextState);
}
