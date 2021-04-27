package chess.dao;

import java.util.Map;

import chess.domain.board.Board;
import chess.domain.chess.Chess;
import chess.domain.position.MovePosition;
import chess.repository.MoveRepository;

public class FakeMoveDao implements MoveRepository {

    private final Map<Long, Chess> fakeChessTable;

    public FakeMoveDao(Map<Long, Chess> fakeChessTable) {
        this.fakeChessTable = fakeChessTable;
    }

    @Override
    public void move(long chessId, MovePosition movePosition) {

    }

    @Override
    public void updateChess(long chessId, String status, String turn) {
        final Board board = fakeChessTable.get(chessId).getBoard();
        final Chess chess = new Chess(board, status, turn);
        fakeChessTable.put(chessId, chess);
    }
}
