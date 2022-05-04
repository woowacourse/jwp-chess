package chess.database.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import chess.database.entity.GameEntity;

public class FakeGameDao implements GameDao {

    private static class FakeRow {

        private String turnColor;
        private String state;
        private final Long roomId;

        public FakeRow(String turnColor, String state, Long roomId) {
            this.turnColor = turnColor;
            this.state = state;
            this.roomId = roomId;
        }

        public String getTurnColor() {
            return turnColor;
        }

        public String getState() {
            return state;
        }

        public Long getRoomId() {
            return roomId;
        }

        public void setTurnColor(String turnColor) {
            this.turnColor = turnColor;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    private final Map<Long, FakeRow> memoryDatabase;
    private long id;

    public FakeGameDao() {
        this.memoryDatabase = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public Long saveGame(GameEntity entity) {
        memoryDatabase.put(id, new FakeRow(entity.getTurnColor(), entity.getState(), entity.getRoomId()));
        return id++;
    }

    @Override
    public void updateGame(GameEntity entity) {
        final FakeRow fakeRow = memoryDatabase.get(entity.getId());
        fakeRow.setState(entity.getState());
        fakeRow.setTurnColor(entity.getTurnColor());
    }

    @Override
    public Optional<GameEntity> findGameById(Long gameId) {
        final FakeRow fakeRow = memoryDatabase.get(gameId);
        return Optional.of(new GameEntity(gameId, fakeRow.getTurnColor(), fakeRow.getState(), fakeRow.getRoomId()));
    }

    @Override
    public Optional<GameEntity> findGameByRoomId(Long roomId) {
        return memoryDatabase.entrySet()
            .stream()
            .filter(entry -> entry.getValue().getRoomId().equals(roomId))
            .map(entry -> new GameEntity(
                entry.getKey(),
                entry.getValue().getTurnColor(),
                entry.getValue().getState(),
                roomId))
            .findAny();
    }
}
