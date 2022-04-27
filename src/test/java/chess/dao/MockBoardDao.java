package chess.dao;

import chess.domain.Color;
import chess.dto.BoardInfoDto;
import chess.dto.CreateBoardDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockBoardDao implements BoardDao {

    private Map<Integer, FakeBoard> fakeBoard = new HashMap<>();

    @Override
    public int makeBoard(Color color, CreateBoardDto boardInfoDto) {
        return 0;
    }

    @Override
    public List<BoardInfoDto> getAllBoardInfo() {
        return null;
    }

    @Override
    public void updateTurn(Color turn, int id) {
    }

    @Override
    public Color findTurn(int id) {
        FakeBoard fakeBoard = this.fakeBoard.get(1);
        return Color.from(fakeBoard.getTurn());
    }

    @Override
    public void deleteBoard(int id) {
        fakeBoard = new HashMap<>();
    }

    @Override
    public boolean existBoard(int id) {
        return false;
    }
}
