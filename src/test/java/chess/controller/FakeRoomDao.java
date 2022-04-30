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
    public void deleteRoom(int roomNumber, String password) {
        RoomDto roomDto = rooms.get(roomNumber);
        if (roomDto.getPassword().equals(password)) {
            rooms.remove(roomNumber);
        }
    }

    @Override
    public List<RoomDto> findAllRoom() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public boolean checkRoom(int roomNumber, String password) {
        return rooms.get(roomNumber)
                .getPassword()
                .equals(password);
    }
}
