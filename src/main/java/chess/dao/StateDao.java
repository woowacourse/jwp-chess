package chess.dao;

import chess.model.board.Board;
import chess.model.state.State;

public interface StateDao {

    void insert(final State state);

    State find(final Board board);

    int delete();

    void update(final State nowState, final State nextState);
}
