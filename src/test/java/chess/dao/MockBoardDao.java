package chess.dao;

import chess.domain.ChessGame2;
import chess.domain.Color;
import chess.domain.Room;
import chess.domain.board.Board;
import java.util.HashMap;
import java.util.Map;

public class MockBoardDao implements BoardDao {

    private Map<Long, FakeBoard> fakeBoard = new HashMap<>();

    @Override
    public Color findTurn(Long id) {
        FakeBoard fakeBoard = this.fakeBoard.get(id);
        return Color.from(fakeBoard.getTurn());
    }

    @Override
    public void deleteBoard() {
        fakeBoard = new HashMap<>();
    }

    @Override
    public boolean existsBoardByName(String title) {
        for (FakeBoard value : fakeBoard.values()) {
            if (title.equals(value.getTitle())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Long save(ChessGame2 chessGame2) {
        final Board board = chessGame2.getBoard();
        final Room room = chessGame2.getRoom();
        fakeBoard.put(1L, new FakeBoard(board.getTurn().name(), room.getTitle(), room.getPassword()));
        return 1L;
    }

    @Override
    public void updateTurn(Long boardId, Color turn) {
        final FakeBoard fakeBoard = this.fakeBoard.get(boardId);
        final String title = fakeBoard.getTitle();
        final String password = fakeBoard.getPassword();
        this.fakeBoard.remove(boardId);
        this.fakeBoard.put(boardId, new FakeBoard(turn.name(), title, password));
    }
}
