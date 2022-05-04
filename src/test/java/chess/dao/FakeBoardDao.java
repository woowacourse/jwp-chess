package chess.dao;

import chess.entity.Square;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FakeBoardDao implements BoardDao {

    private final List<Square> board;

    public FakeBoardDao() {
        this.board = new ArrayList<>();
    }

    @Override
    public void save(List<Square> squares) {
        this.board.addAll(squares);
    }

    @Override
    public List<Square> findById(int id) {
        return board.stream()
                .filter(square -> square.getGameId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public int update(Square square) {
        Square square1 = board.stream()
                .filter(it -> it.getGameId() == square.getGameId() && it.getPosition().equals(square.getPosition()))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        board.remove(square1);

        board.add(square);
        return square.getGameId();
    }

    @Override
    public void delete(int gameId) {
        board.remove(gameId);
    }
}
