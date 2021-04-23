package chess.dao;

import java.util.Map;

import chess.domain.chess.Chess;
import chess.domain.chess.Color;
import chess.domain.chess.Status;
import chess.domain.position.MovePosition;
import chess.repository.MoveRepository;

public class FakeMoveDao implements MoveRepository {

    private final Map<Long, Chess> fakeChessTable;

    public FakeMoveDao(Map<Long, Chess> fakeChessTable) {
        this.fakeChessTable = fakeChessTable;
    }

    @Override
    public void move(long chessId, MovePosition movePosition) {
        final Chess chess = fakeChessTable.get(chessId);
        chess.move(movePosition);
        fakeChessTable.put(chessId, chess);
    }

    @Override
    public void updateChess(long chessId, String status, String turn) {
        Chess chess = fakeChessTable.get(chessId);
        chess = new Chess(chess.getBoard(), Status.valueOf(status), Color.valueOf(turn));
        fakeChessTable.put(chessId, chess);
    }
}
