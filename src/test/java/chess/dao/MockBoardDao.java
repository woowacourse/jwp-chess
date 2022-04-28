package chess.dao;

import chess.domain.Color;
import chess.dto.BoardInfoDto;
import chess.dto.CreateBoardDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MockBoardDao implements BoardDao {

    private Map<Integer, FakeBoard> fakeBoard = new HashMap<>();
    int ID = 1;

    @Override
    public int makeBoard(Color color, CreateBoardDto boardInfoDto) {
        fakeBoard.put(ID, new FakeBoard(color.name(), boardInfoDto.getName(), boardInfoDto.getPassword()));
        return ID++;
    }

    @Override
    public List<BoardInfoDto> getAllBoardInfo() {
        return fakeBoard.values().stream()
                .map(fakeBoard -> new BoardInfoDto(1, fakeBoard.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTurn(Color turn, int id) {
        FakeBoard oldBoard = this.fakeBoard.get(id);
        fakeBoard.put(id, new FakeBoard(turn.name(), oldBoard.getName(), oldBoard.getPassword()));
    }

    @Override
    public Color findTurn(int id) {
        FakeBoard fakeBoard = this.fakeBoard.get(1);
        System.out.println(fakeBoard.getTurn());
        return Color.from(fakeBoard.getTurn());
    }

    @Override
    public void deleteBoard(int id, String password) {
        fakeBoard = new HashMap<>();
    }

    @Override
    public boolean isGameEnd(int id) {
        return fakeBoard.isEmpty();
    }

    @Override
    public void end(int id) {
        fakeBoard.remove(id);
    }

    @Override
    public String getPassword(int id) {
        FakeBoard board = this.fakeBoard.get(id);
        return board.getPassword();
    }
}
