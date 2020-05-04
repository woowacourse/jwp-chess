package wooteco.chess.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.RequestDto.RoomExitRequestDto;
import wooteco.chess.dto.RequestDto.RoomJoinRequestDto;
import wooteco.chess.dto.ResponseDto.ResponseDto;
import wooteco.chess.dto.RequestDto.RoomCreateRequestDto;
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

    public ResponseDto create(RoomCreateRequestDto requestDto){
        if(roomRepository.findByName(requestDto.getRoomName()).isPresent()) {
            return ResponseDto.fail(ChessResponseCode.ROOM_IS_ALREADY_EXIST);
        }
        return ResponseDto.success(roomRepository.save(requestDto.toEntity()).getId());
    }

    public ResponseDto status(Long roomId){
        Room room = roomRepository.findById(roomId)
                .orElse(null);
        if(Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }
        return ResponseDto.success(room);
    }

    @Transactional
    public ResponseDto join(RoomJoinRequestDto requestDto){
        Room room = roomRepository.findByName(requestDto.getRoomName())
                .orElse(null);

        if (Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (room.getWhitePassword().equals(DEFAULT_VALUE)) {
            room.setWhitePassword(requestDto.getUserPassword());
        } else if (room.getBlackPassword().equals(DEFAULT_VALUE)) {
            room.setBlackPassword(requestDto.getUserPassword());
        } else {
            return ResponseDto.fail(ChessResponseCode.ROOM_IS_FULL);
        }

        return ResponseDto.success(roomRepository.save(room));
    }

    @Transactional
    public ResponseDto exit(RoomExitRequestDto requestDto) {
        Room room = roomRepository.findById(requestDto.getId())
                .orElse(null);

        if (Objects.isNull(room)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        if (room.getWhitePassword().equals(requestDto.getUserPassword())) {
            room.setWhitePassword(DEFAULT_VALUE);
        } else if (room.getBlackPassword().equals(requestDto.getUserPassword())) {
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
