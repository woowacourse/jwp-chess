package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.entity.Room;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.support.ChessResponseCode;

import java.sql.SQLException;
import java.util.Objects;

@Service
public class RoomService {
    private static final Long DEFAULT_VALUE = -1L;

    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ResponseDto create(Room room){
        return ResponseDto.success(roomRepository.save(room));
    }

    public ResponseDto status(Long roomId) throws SQLException {
        return ResponseDto.success(roomRepository.findById(roomId));
    }

    public ResponseDto join(String roomName, Long userId){
        Room room = roomRepository.findByName(roomName)
                .orElse(null);

        if (Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (room.getWhiteUserId() == DEFAULT_VALUE) {
            room.setWhiteUserId(userId);
        } else if (room.getBlackUserId() == DEFAULT_VALUE) {
            room.setBlackUserId(userId);
        } else {
            return ResponseDto.fail(ChessResponseCode.ROOM_IS_FULL);
        }

        roomRepository.save(room);
        return ResponseDto.success();
    }

    public ResponseDto exit(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElse(null);

        if (Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (room.getWhiteUserId() == userId) {
            room.setWhiteUserId(DEFAULT_VALUE);
        } else if (room.getBlackUserId() == userId) {
            room.setBlackUserId(DEFAULT_VALUE);
        } else {
            return ResponseDto.fail(ChessResponseCode.BAD_REQUEST);
        }

        roomRepository.save(room);
        return ResponseDto.success();
    }
}
