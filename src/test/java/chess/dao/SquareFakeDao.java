package chess.dao;

import chess.entity.Square;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class SquareFakeDao implements SquareDao {

    private final Map<Long, Square> memoryDbSquare = new HashMap<>();
    private Long autoIncrementId = 0L;

    @Override
    public int[] insertSquareAll(Long roomId, List<Square> squares) {
        int autoId = 0;
        int[] numbers = new int[squares.size()];
        for (Square square : squares) {
            numbers[autoId] = autoId++;
            memoryDbSquare.put(autoIncrementId++, square);
        }
        return numbers;
    }

    @Override
    public List<Square> findSquareAllById(Long roomId) {
        return memoryDbSquare.entrySet().stream()
                .filter(entry -> entry.getValue().getRoomId() == roomId)
                .map(entry -> new Square(
                        entry.getKey(), entry.getValue().getRoomId(), entry.getValue().getPosition(),
                        entry.getValue().getSymbol(), entry.getValue().getColor()))
                .collect(Collectors.toList());
    }

    @Override
    public Long updateSquare(Square square) {
        final List<Entry<Long, Square>> entries = memoryDbSquare.entrySet().stream()
                .filter(entry -> entry.getValue().getRoomId() == square.getRoomId()
                        && entry.getValue().getPosition().equals(square.getPosition()))
                .collect(Collectors.toList());

        memoryDbSquare.put(entries.get(0).getKey(), square);
        return square.getRoomId();
    }

    @Override
    public Long deleteSquareAllById(Long roomId) {
        final List<Entry<Long, Square>> entries = memoryDbSquare.entrySet().stream()
                .filter(entry -> entry.getValue().getRoomId() == roomId)
                .collect(Collectors.toList());
        for (Entry<Long, Square> entry : entries) {
            memoryDbSquare.remove(entry.getKey());
        }
        return roomId;
    }
}
