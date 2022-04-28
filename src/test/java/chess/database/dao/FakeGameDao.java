package chess.database.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import chess.database.dto.GameStateDto;

public class FakeGameDao implements GameDao {

    private static class FakeTable {
        private GameStateDto dto;
        private String roomName;
        private String password;

        public FakeTable(GameStateDto dto, String roomName, String password) {
            this.dto = dto;
            this.roomName = roomName;
            this.password = password;
        }

        public GameStateDto getDto() {
            return dto;
        }

        public String getRoomName() {
            return roomName;
        }

        public String getPassword() {
            return password;
        }

        public void setDto(GameStateDto dto) {
            this.dto = dto;
        }
    }

    private final Map<Long, FakeTable> memoryDatabase;
    private long id;

    public FakeGameDao() {
        this.memoryDatabase = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public Optional<GameStateDto> findGameById(Long id) {
        final FakeTable table = memoryDatabase.get(id);
        if (table == null) {
            return Optional.empty();
        }
        return Optional.of(table.getDto());
    }

    @Override
    public Long saveGame(GameStateDto gameStateDto, String roomName, String password) {
        FakeTable table = new FakeTable(gameStateDto, roomName, password);
        memoryDatabase.put(id, table);
        return id++;
    }

    @Override
    public void updateState(GameStateDto gameStateDto, Long id) {
        final FakeTable table = memoryDatabase.get(id);
        if (table == null) {
            return;
        }
        table.setDto(gameStateDto);
    }

    @Override
    public void removeGame(Long id) {
        memoryDatabase.remove(id);
    }

    @Override
    public Optional<GameStateDto> findGameByRoomName(String roomName) {
        return memoryDatabase.values().stream()
            .filter(table -> table.getRoomName().equals(roomName))
            .map(FakeTable::getDto)
            .findAny();
    }

    @Override
    public Map<Long, String> readGameRoomIdAndNames() {
        return null;
    }

    @Override
    public Optional<String> findPasswordById(Long roomId) {
        return Optional.empty();
    }
}
