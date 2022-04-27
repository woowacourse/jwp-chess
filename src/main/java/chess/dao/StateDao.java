package chess.dao;

import chess.model.board.Board;
import chess.model.state.State;

public interface StateDao {

    void insert(Long id, final State state);

    State find(Long id, final Board board);

    int delete(Long id);

    void update(Long id, final State nowState, final State nextState);
}
