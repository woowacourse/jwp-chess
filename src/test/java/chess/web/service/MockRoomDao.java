package chess.web.service;

import chess.domain.board.Team;
import chess.domain.board.Turn;
import chess.domain.entity.Room;
import chess.web.dao.RoomDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MockRoomDao implements RoomDao {
    static class Data {
        private final String turn;
        private final String title;
        private final String password;

        private Data(String turn, String title, String password) {
            this.turn = turn;
            this.title = title;
            this.password = password;
        }
    }

    private final Map<Long, Data> mockDb = new HashMap<>();

    public MockRoomDao() {
        mockDb.put(1L, new Data("white", "제목", "비밀번호"));
        mockDb.put(2L, new Data("white", "제목2", "비밀번호"));
    }

    @Override
    public Optional<Turn> findTurnById(Long id) {
        Data data = mockDb.get(id);
        Team team = Team.from(data.turn);
        return Optional.of(new Turn(team));
    }

    @Override
    public void updateTurnById(Long id, String newTurn) {
        Data data = mockDb.get(id);
        mockDb.put(id, new Data(newTurn, data.title, data.password));
    }

    @Override
    public Long save(String title, String password) {
        return 1L;
    }

    @Override
    public Optional<Room> findById(Long id) {
        Data data = mockDb.get(id);
        return Optional.of(new Room(id, data.title, data.password));
    }

    @Override
    public void deleteById(Long id) {
        mockDb.remove(id);
    }

    @Override
    public List<Room> findAll() {
        Data data = mockDb.get(1L);
        Room room = new Room(1L, data.title, data.password);
        return List.of(room);
    }

    @Override
    public boolean existByTitle(String title) {
        return "제목".equals(title);
    }
}
