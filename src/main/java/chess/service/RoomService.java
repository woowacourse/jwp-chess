package chess.service;

import chess.domain.Room;
import chess.repository.BoardDao;
import chess.repository.RoomDao;
import chess.dto.RoomDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;
    private final BoardDao boardDao;

    public RoomService(RoomDao roomDao, BoardDao boardDao) {
        this.roomDao = roomDao;
        this.boardDao = boardDao;
    }

    public RoomDto create(Room room) {
        int id = roomDao.save(room.getName(), room.getPassword());
        return roomDao.findById(id).get();
    }

    public void delete(int roomId, String password) {
        Optional<Integer> boardId = boardDao.findBoardIdByRoom(roomId);
        Optional<RoomDto> room = roomDao.findById(roomId);
        checkPassword(room, password);
        if (boardId.isEmpty()) {
            roomDao.delete(roomId);
            return;
        }
        checkGameEnd(boardId);
        roomDao.delete(roomId);
    }

    public List<Map<String, String>> findRooms() {
        List<RoomDto> data = roomDao.findAll();
        return data.stream()
                .map(room -> Map.of("id", String.valueOf(room.getId()), "name", room.getName()))
                .collect(Collectors.toList());
    }

    public void validateId(int roomId) {
        Optional<RoomDto> roomDto = roomDao.findById(roomId);
        if (roomDto.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 체스방 주소입니다.");
        }

    }

    private void checkGameEnd(Optional<Integer> boardId) {
        if (!boardDao.getEnd(boardId.get())) {
            throw new IllegalArgumentException("진행 중인 게임은 삭제할 수 없습니다.");
        }
    }

    private void checkPassword(Optional<RoomDto> room, String password) {
        if (!room.get().getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

}
