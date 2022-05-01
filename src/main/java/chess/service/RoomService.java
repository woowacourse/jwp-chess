package chess.service;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
import chess.entity.Room;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private static final String DEFAULT_GAME_STATE = "ready";
    private static final String FIRST_TURN = "WHITE";

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public void saveNewRoom(final String roomName, final String passWord) {
        if (roomDao.hasDuplicatedName(roomName)) {
            throw new IllegalArgumentException("이미 동일한 이름의 체스방이 존재합니다.");
        }
        roomDao.saveNewRoom(roomName, passWord, DEFAULT_GAME_STATE, FIRST_TURN);
    }

    public void deleteRoom(final int roomId, final String password) {
        final Room savedRoom = roomDao.findByRoomId(roomId);

        savedRoom.validatePassword(password);
        if (savedRoom.isPlayingState()) {
            throw new IllegalStateException("게임이 진행중인 체스방은 삭제할 수 없습니다.");
        }
        roomDao.deleteRoomByName(roomId);
    }

    public List<RoomDto> getRoomNames() {
        final List<Room> rooms = roomDao.findAllRooms();

        return rooms.stream()
                .map(room -> new RoomDto(room.getName(), room.getRoomId()))
                .collect(Collectors.toUnmodifiableList());
    }
}
