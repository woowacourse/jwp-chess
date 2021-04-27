package chess.dao;

import java.util.HashMap;
import java.util.Map;

import chess.domain.chess.Chess;
import chess.repository.ChessRepository;

public class FakeChessDao implements ChessRepository {

    private final Map<Long, Chess> fakeChessTable;

    public FakeChessDao() {
        this.fakeChessTable = new HashMap<>();
    }

    public FakeChessDao(Map<Long, Chess> fakeChessTable) {
        this.fakeChessTable = fakeChessTable;
    }

    @Override
    public Chess findChessById(long chessId) {
        return fakeChessTable.get(chessId);
    }
}
