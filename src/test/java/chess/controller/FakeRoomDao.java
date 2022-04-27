package chess.controller;

import chess.dao.RoomDao;
import chess.dto.RoomDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeRoomDao implements RoomDao {

    private final Map<Integer, RoomDto> rooms = new HashMap<>();
    private int id = 0;

    @Override
    public void createRoom(String roomName, String password) {
        rooms.put(++id, new RoomDto(id, roomName, password));
    }

    @Override
    public void deleteRoom(int roomId, String password) {
        RoomDto roomDto = rooms.get(roomId);
        if (roomDto.getPassword().equals(password)) {
            rooms.remove(roomId);
        }
    }

    @Override
    public List<RoomDto> findAllRoom() {
        return new ArrayList<>(rooms.values());
    }
}
