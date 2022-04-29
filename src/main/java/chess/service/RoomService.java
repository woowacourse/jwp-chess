package chess.service;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
import chess.entity.RoomEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private static final String DEFAULT_GAME_STATE = "ready";
    private static final String FIRST_TURN = "WHITE";
    private static final String PLAYING_STATE_SYMBOL = "playing";

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
        final RoomEntity savedRoom = roomDao.findByRoomId(roomId);

        if (isIncorrectPassword(savedRoom, password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (isPlayingState(savedRoom)) {
            throw new IllegalStateException("게임이 진행중인 체스방은 삭제할 수 없습니다.");
        }
        roomDao.deleteRoomByName(roomId);
    }

    private boolean isIncorrectPassword(final RoomEntity room, final String password) {
        final String savedPassword = room.getPassword();
        return !password.equals(savedPassword);
    }

    private boolean isPlayingState(final RoomEntity room) {
        final String savedGameState = room.getGameState();
        return savedGameState.equals(PLAYING_STATE_SYMBOL);
    }

    public List<RoomDto> getRoomNames() {
        final List<RoomEntity> rooms = roomDao.findAllRooms();

        return rooms.stream()
                .map(room -> new RoomDto(room.getName(), room.getRoomId()))
                .collect(Collectors.toUnmodifiableList());
    }
}
