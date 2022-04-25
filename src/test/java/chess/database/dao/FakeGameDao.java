package chess.database.dao;

import chess.database.dto.GameStateDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeGameDao implements GameDao {

    private static final int STATE_INDEX = 0;
    private static final int COLOR_INDEX = 1;

    private final Map<Integer, List<String>> memoryDatabase;

    public FakeGameDao() {
        this.memoryDatabase = new HashMap<>();
    }

    @Override
    public List<String> readStateAndColor(int roomId) {
        final List<String> stateAndColor = memoryDatabase.get(roomId);
        if (stateAndColor == null) {
            return new ArrayList<>();
        }
        return stateAndColor;
    }

    @Override
    public void updateState(GameStateDto gameStateDto, int roomId) {
        List<String> target = memoryDatabase.get(roomId);
        target.set(STATE_INDEX, gameStateDto.getState());
        target.set(COLOR_INDEX, gameStateDto.getTurnColor());
    }

    @Override
    public void removeGame(int roomId) {
        memoryDatabase.remove(roomId);
    }

    @Override
    public void create(GameStateDto gameStateDto, int roomId) {
        memoryDatabase.put(roomId,
            Arrays.asList(gameStateDto.getState(), gameStateDto.getTurnColor()));
    }
}
