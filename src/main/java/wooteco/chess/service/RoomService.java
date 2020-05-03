package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.entity.Room;
import wooteco.chess.repository.RoomRepository;
import wooteco.chess.support.ChessResponseCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RoomService {
    private static final String DEFAULT_VALUE = "default";

    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public ResponseDto create(Room room){
        return ResponseDto.success(roomRepository.save(room));
    }

    public ResponseDto status(Long roomId){
        Room room = roomRepository.findById(roomId)
                .orElse(null);
        if(Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }
        return ResponseDto.success(room);
    }

    public ResponseDto join(String roomName, String userPassword){
        Room room = roomRepository.findByName(roomName)
                .orElse(null);

        if (Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (room.getWhitePassword().equals(DEFAULT_VALUE)) {
            room.setWhitePassword(userPassword);
        } else if (room.getBlackPassword().equals(DEFAULT_VALUE)) {
            room.setBlackPassword(userPassword);
        } else {
            return ResponseDto.fail(ChessResponseCode.ROOM_IS_FULL);
        }

        return ResponseDto.success(roomRepository.save(room));
    }

    public ResponseDto exit(Long roomId, String userPassword) {
        Room room = roomRepository.findById(roomId)
                .orElse(null);

        if (Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (room.getWhitePassword().equals(userPassword)) {
            room.setWhitePassword(DEFAULT_VALUE);
        } else if (room.getBlackPassword().equals(userPassword)) {
            room.setBlackPassword(DEFAULT_VALUE);
        } else {
            return ResponseDto.fail(ChessResponseCode.BAD_REQUEST);
        }

        roomRepository.save(room);
        return ResponseDto.success();
    }

    public ResponseDto getRooms() {
        List<String> rooms = new ArrayList<>();
        roomRepository.findAll().forEach(room -> rooms.add(room.getName()));
        return ResponseDto.success(rooms);
    }

    public Team checkAuthentication(Long roomId, String userPassword) {
        Room room = roomRepository.findById(roomId)
                .orElse(null);
        if (Objects.isNull(room)) {
            return Team.NOTHING;
        }

        if(room.getWhitePassword().equals(userPassword)) {
            return Team.WHITE;
        }
        if (room.getBlackPassword().equals(userPassword)) {
            return Team.BLACK;
        }
        return Team.NOTHING;
    }
}
