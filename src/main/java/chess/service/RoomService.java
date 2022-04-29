package chess.service;

import chess.domain.Room;
import chess.repository.BoardRepository;
import chess.repository.RoomRepository;
import chess.web.dto.RoomDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BoardRepository boardRepository;

    public RoomService(RoomRepository roomRepository, BoardRepository boardRepository) {
        this.roomRepository = roomRepository;
        this.boardRepository = boardRepository;
    }

    public RoomDto create(Room room) {
        int id = roomRepository.save(room.getName(), room.getPassword());
        return roomRepository.findById(id).get();
    }

    public void delete(int roomId, String password) {
        Optional<Integer> boardId = boardRepository.findBoardIdByRoom(roomId);
        Optional<RoomDto> room = roomRepository.findById(roomId);
        if (!room.get().getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (boardId.isEmpty()) {
            roomRepository.delete(roomId);
            return;
        }
        if (!boardRepository.getEnd(boardId.get())) {
            throw new IllegalArgumentException("진행 중인 게임은 삭제할 수 없습니다.");
        }
        roomRepository.delete(roomId);
    }

    public List<Map<String, String>> findRooms() {
        List<RoomDto> data = roomRepository.findAll();
        return data.stream()
                .map(room -> Map.of("id", String.valueOf(room.getId()), "name", room.getName()))
                .collect(Collectors.toList());
    }

    public void validateId(int roomId) {
        Optional<RoomDto> roomDto = roomRepository.findById(roomId);
        if (roomDto.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 체스방 주소입니다.");
        }

    }
}
