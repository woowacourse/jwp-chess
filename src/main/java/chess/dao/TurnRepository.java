package chess.dao;

import org.springframework.stereotype.Repository;

@Repository
public class TurnRepository implements TurnDao {

    @Override
    public String getCurrentTurn() {
        throw new UnsupportedOperationException("TurnRepository#getCurrentTurn not implemented.");
    }

    @Override
    public void updateTurn(final String currentTurn, final String previousTurn) {
        throw new UnsupportedOperationException("TurnRepository#updateTurn not implemented.");
    }
}
