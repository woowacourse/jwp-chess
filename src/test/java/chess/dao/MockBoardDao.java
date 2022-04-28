package chess.dao;

import chess.domain.ChessGame;
import chess.domain.Color;
import chess.domain.Room;
import chess.domain.board.Board;
import chess.dto.RoomDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockBoardDao implements BoardDao {

    private Map<Long, FakeBoard> fakeBoard = new HashMap<>();

    @Override
    public Color findTurn(Long id) {
        FakeBoard fakeBoard = this.fakeBoard.get(id);
        return Color.from(fakeBoard.getTurn());
    }

    @Override
    public void deleteBoard(Long boardId) {
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
    public Long save(ChessGame chessGame) {
        final Board board = chessGame.getBoard();
        final Room room = chessGame.getRoom();
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

    @Override
    public List<RoomDto> findAllRooms() {
        return null;
    }

    @Override
    public String findPasswordById(Long boardId) {
        return null;
    }
}
