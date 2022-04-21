package chess.dao;

import chess.entity.BoardEntity;
import chess.util.JdbcTestFixture;
import java.util.List;

public class FakeBoardDao implements BoardDao {

    public List<BoardEntity> fakeBoard;

    public FakeBoardDao() {
        fakeBoard = JdbcTestFixture.getMovedTestBoard();
    }

    @Override
    public List<BoardEntity> getBoard() {
        return fakeBoard;
    }

    @Override
    public void updatePosition(BoardEntity board) {
        fakeBoard.stream()
            .filter(it -> it.getPosition().equals(board.getPosition()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("[ERROR] 데이터가 존재하지 않습니다."))
            .update(board.getPiece());
    }

    @Override
    public void updateBatchPositions(List<BoardEntity> board) {
        fakeBoard = board;
    }
}
