package chess.dao;

import chess.entity.Square;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
