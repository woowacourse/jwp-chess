package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.repository.CachedRoomRepository;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.support.ChessResponseCode;

import java.sql.SQLException;
import java.util.Objects;

@Service
public class RoomService {
    private static final Long DEFAULT_VALUE = -1L;

    private RoomRepository roomRepository;

    public RoomService(CachedRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ResponseDto create(RoomDto roomDto) throws SQLException {
        return ResponseDto.success(roomRepository.create(roomDto));
    }

    public ResponseDto status(Long roomId) throws SQLException {
        return ResponseDto.success(roomRepository.findById(roomId));
    }

    public ResponseDto join(String roomName, Long userId) throws SQLException {
        RoomDto roomDto = roomRepository.findByName(roomName);

        if (Objects.isNull(roomDto)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (roomDto.getWhiteUserId() == DEFAULT_VALUE) {
            roomDto.setWhiteUserId(userId);
        } else if (roomDto.getBlackUserId() == DEFAULT_VALUE) {
            roomDto.setBlackUserId(userId);
        } else {
            return ResponseDto.fail(ChessResponseCode.ROOM_IS_FULL);
        }

        roomRepository.update(roomDto);
        return ResponseDto.success();
    }

    public ResponseDto exit(Long roomId, Long userId) throws SQLException {
        RoomDto roomDto = roomRepository.findById(roomId);

        if (Objects.isNull(roomDto)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (roomDto.getWhiteUserId() == userId) {
            roomDto.setWhiteUserId(DEFAULT_VALUE);
        } else if (roomDto.getBlackUserId() == userId) {
            roomDto.setBlackUserId(DEFAULT_VALUE);
        } else {
            return ResponseDto.fail(ChessResponseCode.BAD_REQUEST);
        }

        roomRepository.update(roomDto);
        return ResponseDto.success();
    }
}
