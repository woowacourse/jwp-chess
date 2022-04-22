package chess.service;

import chess.dao.RoomRepository;
import chess.web.dto.RoomDto;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private static final int NAME_MIN_SIZE = 1;
    private static final int NAME_MAX_SIZE = 16;
    private static final String ERROR_NAME_SIZE = "방 이름은 1자 이상, 16자 이하입니다.";

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomDto create(String name) {
        validateNameSize(name);
        Optional<RoomDto> roomDto = roomRepository.find(name);
        if (roomDto.isEmpty()) {
            roomRepository.save(name);
        }
        return roomRepository.find(name).get();
    }

    private void validateNameSize(String name) {
        if (name.length() < NAME_MIN_SIZE || name.length() > NAME_MAX_SIZE) {
            throw new IllegalArgumentException(ERROR_NAME_SIZE);
        }
    }
}
