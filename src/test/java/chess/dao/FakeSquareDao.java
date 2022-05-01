package chess.dao;

import chess.entity.SquareEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeSquareDao implements SquareDao {

    private final Map<Long, List<SquareEntity>> value = new HashMap<>();

    @Override
    public void saveAll(List<SquareEntity> squares, long roomId) {
        value.put(roomId, squares);
    }

    @Override
    public List<SquareEntity> findByRoomId(long roomId) {
        return value.getOrDefault(roomId, List.of());
    }

    @Override
    public Optional<SquareEntity> findByRoomIdAndPosition(long roomId, String position) {
        List<SquareEntity> squares = value.get(roomId);
        return squares.stream()
                .filter(square -> square.getPosition().equals(position))
                .findAny();
    }

    @Override
    public void update(long roomId, String position, String piece) {
        List<SquareEntity> squares = value.get(roomId);
        List<SquareEntity> newSquares = squares.stream()
                .filter(square -> square.getPosition().equals(position))
                .map(square -> new SquareEntity(position, piece))
                .collect(Collectors.toList());
        value.replace(roomId, newSquares);
    }

    @Override
    public void removeAll(long roomId) {
        value.remove(roomId);
    }
}
