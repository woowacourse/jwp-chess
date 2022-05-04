package chess.web.service.fakedao;

import chess.domain.room.RoomName;
import chess.domain.room.RoomPassword;
import chess.web.dao.RoomDao;
import chess.web.dto.RoomDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeRoomDao implements RoomDao {

    Map<Integer, RoomName> map = new HashMap<>();

    @Override
    public int save(RoomName name, RoomPassword password) {
        map.put(1, name);
        return 1;
    }

    @Override
    public List<RoomDto> findAll() {
        List<RoomDto> roomDtos = new ArrayList<>();
        for (int roomNumber : map.keySet()) {
            roomDtos.add(RoomDto.of(roomNumber, map.get(roomNumber)));
        }
        return roomDtos;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public boolean confirmPassword(int id, String password) {
        return false;
    }
}
