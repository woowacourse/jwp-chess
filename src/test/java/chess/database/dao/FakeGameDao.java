package chess.database.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.database.dto.GameStateDto;

public class FakeGameDao implements GameDao {

    private static final int STATE_INDEX = 0;
    private static final int COLOR_INDEX = 1;

    private final Map<String, List<String>> memoryDatabase;

    public FakeGameDao() {
        this.memoryDatabase = new HashMap<>();
    }

    @Override
    public List<String> readStateAndColor(String roomName) {
        final List<String> stateAndColor = memoryDatabase.get(roomName);
        if (stateAndColor == null) {
            return new ArrayList<>();
        }
        return stateAndColor;
    }

    @Override
    public void saveGame(GameStateDto gameStateDto, String roomName) {
        memoryDatabase.put(roomName, Arrays.asList(gameStateDto.getState(), gameStateDto.getTurnColor()));
    }

    @Override
    public void updateState(GameStateDto gameStateDto, String roomName) {
        List<String> target = memoryDatabase.get(roomName);
        target.set(STATE_INDEX, gameStateDto.getState());
        target.set(COLOR_INDEX, gameStateDto.getTurnColor());
    }

    @Override
    public void removeGame(String roomName) {
        memoryDatabase.remove(roomName);
    }
}
