package chess;

import chess.dao.SquareDao;
import chess.entity.Square;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeSquareDao implements SquareDao {

    private final Map<Long, List<Square>> value = new HashMap<>();

    @Override
    public void saveAll(List<Square> squares, long roomId) {
        value.put(roomId, squares);
    }

    @Override
    public List<Square> findByRoomId(long roomId) {
        return value.getOrDefault(roomId, List.of());
    }

    @Override
    public Optional<Square> findByRoomIdAndPosition(long roomId, String position) {
        List<Square> squares = value.get(roomId);
        return squares.stream()
                .filter(square -> square.getPosition().equals(position))
                .findAny();
    }

    @Override
    public void update(long roomId, String position, String piece) {
        List<Square> squares = value.get(roomId);
        List<Square> newSquares = squares.stream()
                .filter(square -> square.getPosition().equals(position))
                .map(square -> new Square(position, piece))
                .collect(Collectors.toList());
        value.replace(roomId, newSquares);
    }

    @Override
    public void removeAll(long roomId) {
        value.remove(roomId);
    }
}
