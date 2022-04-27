package chess.service;

import chess.dao.RoomDao;
import chess.domain.Team;
import chess.entity.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeRoomDaoImpl implements RoomDao {

    private final Map<Long, Room> rooms = new HashMap<>();

    @Override
    public Room findById(Long roomId) {
        return rooms.get(roomId);
    }

    @Override
    public void deleteBy(Long roomId, String title) {
        Room room = rooms.get(roomId);
        if (room.getPassword().equals(title)) {
            rooms.remove(roomId);
            return;
        }
        throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
    }

    @Override
    public Long save(Room room) {
        rooms.put(room.getId(), room);
        return room.getId();
    }

    @Override
    public void updateTeam(Team team, Long roomId) {
        Room room = rooms.get(roomId);
        Room updatedRoom = new Room(room.getId(), team, room.getTitle(), room.getPassword(),
            room.getStatus());
        rooms.put(roomId, updatedRoom);
    }

    @Override
    public List<Room> findAll() {
        return rooms.keySet().stream()
            .map(rooms::get)
            .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(Long roomId) {
        Room room = rooms.get(roomId);
        Room endRoom = new Room(room.getId(), room.getTeam(), room.getTitle(), room.getPassword(),
            false);
        rooms.put(roomId, endRoom);
    }
}
