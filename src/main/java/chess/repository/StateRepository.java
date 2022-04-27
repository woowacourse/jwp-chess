package chess.repository;

import chess.dao.StateDao;
import chess.dao.converter.StringToStateConverter;
import chess.model.board.Board;
import chess.model.state.State;
import org.springframework.stereotype.Repository;

@Repository
public class StateRepository {

    private final StateDao stateDao;

    public StateRepository(StateDao stateDao) {
        this.stateDao = stateDao;
    }

    public void initStateData(final String id, final State state) {
        stateDao.deleteFrom(id);
        stateDao.insert(id, state);
    }

    public void updateStateData(final String id, final State nowState, final State nextState) {
        stateDao.update(id, nowState, nextState);
    }

    public State getStateFrom(final String id, final Board board) {
        return StringToStateConverter.convert(stateDao.find(id), board);
    }

    public void deleteStateDataFrom(final String id) {
        stateDao.deleteFrom(id);
    }
}
