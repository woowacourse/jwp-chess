package chess.service;

import chess.controller.dto.RoomDto;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RoomService {

    private static final int ROOM_NAME_LENGTH_BOUND = 10;
    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public Long save(final String roomName) {
        final boolean isDuplicated = roomDao.load().stream()
                .map(RoomDto::getName)
                .anyMatch(name -> name.equals(roomName));

        validateRoomName(roomName, isDuplicated);

        final Long roomId = roomDao.save(roomName);
        return roomId;
    }

    private void validateRoomName(String roomName, boolean isDuplicated) {
        if (isDuplicated) {
            throw new IllegalArgumentException("중복된 방 이름입니다.");
        }

        if (roomName.length() > ROOM_NAME_LENGTH_BOUND) {
            throw new IllegalArgumentException("방 이름은 10 글자 이하로 입력해주세요.");
        }
    }

    public void delete(final Long roomId) {
        roomDao.delete(roomId);
    }

    public List<RoomDto> loadList() {
        return roomDao.load();
    }

    public RoomDto room(final Long roomId) throws SQLException {
        return new RoomDto(roomId, roomDao.name(roomId));
    }
}
