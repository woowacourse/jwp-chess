package chess.service;

import chess.domain.repository.room.RoomRepository;
import chess.domain.room.Room;
import chess.domain.user.User;
import chess.exception.RoomNotFoundException;
import chess.service.dto.RoomAndGameIdDto;
import chess.service.dto.game.RoomJoinDto;
import chess.service.dto.game.RoomSaveDto;
import chess.service.dto.room.PlayingRoomDto;
import chess.service.dto.room.RoomDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final GameService gameService;
    private final UserService userService;
    private final RoomRepository roomRepository;

    public RoomService(GameService gameService, UserService userService, RoomRepository roomRepository) {
        this.gameService = gameService;
        this.userService = userService;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomAndGameIdDto saveRoom(final RoomSaveDto roomSaveDto) {
        Long gameId = gameService.saveNewGame();
        User whiteUser = new User(roomSaveDto.getWhiteUsername(), roomSaveDto.getWhitePassword());
        Long whiteUserId = userService.save(whiteUser);
        Long roomId = roomRepository.save(new Room(gameId, roomSaveDto.getRoomName(), whiteUserId));
        return new RoomAndGameIdDto(roomId, gameId);
    }

    @Transactional
    public Long joinGame(final Long roomId, final RoomJoinDto roomJoinDto) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        Room room = roomOptional.orElseThrow(() -> new IllegalArgumentException("해당 방을 찾을 수 없습니다."));
        User user = new User(roomJoinDto.getUsername(), roomJoinDto.getPassword());
        return joinUser(room, user);
    }

    private Long joinUser(final Room room, final User user) {
        if (room.isFullRoom()) {
            userService.checkAccessibleUser(room.getId(), user);
            return room.getId();
        }
        if (room.isAccessibleRoom()) {
            return joinBlackUser(user, room);
        }
        throw new IllegalArgumentException("방에 접속할 수 없습니다.");
    }

    private Long joinBlackUser(final User user, final Room room) {
        Long userId = userService.save(user);
        Room updatedRoom = room.joinBlackUser(userId);
        roomRepository.update(updatedRoom);
        return room.getId();
    }

    @Transactional(readOnly = true)
    public List<PlayingRoomDto> findRoomsByPlaying() {
        List<Room> rooms = roomRepository.findByPlayingGame();
        return rooms.stream()
                .map(this::playingRoom)
                .collect(Collectors.toList());
    }

    private PlayingRoomDto playingRoom(final Room room) {
        User whiteUser = userService.findById(room.whiteUserId());
        if (room.isAccessibleRoom()) {
            return new PlayingRoomDto(room.getId(), room.name(), whiteUser.getName(), "");
        }
        User blackUser = userService.findById(room.blackUserId());
        return new PlayingRoomDto(room.getId(), room.name(), whiteUser.getName(), blackUser.getName());
    }

    @Transactional(readOnly = true)
    public RoomDto findById(final Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        Room room = roomOptional.orElseThrow(() -> new RoomNotFoundException("해당 방을 찾을 수 없습니다."));
        User whiteUser = userService.findById(room.whiteUserId());
        if (room.isAccessibleRoom()) {
            return RoomDto.from(room, whiteUser.getName());
        }
        User blackUser = userService.findById(room.blackUserId());
        return RoomDto.from(room, whiteUser.getName(), blackUser.getName());
    }

    @Transactional(readOnly = true)
    public Room findByGameId(final Long gameId) {
        return roomRepository.findByGameId(gameId);
    }
}
