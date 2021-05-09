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
        Long roomId = roomRepository.saveNewRoom(new Room(gameId, roomSaveDto.getRoomName(), whiteUserId));
        return new RoomAndGameIdDto(roomId, gameId);
    }

    @Transactional
    public Long joinGame(final Long roomId, final RoomJoinDto roomJoinDto) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        User user = new User(roomJoinDto.getUsername(), roomJoinDto.getPassword());
        if (roomOptional.isPresent()) {
            return joinUser(roomOptional.get(), user);
        }
        throw new IllegalArgumentException("방에 접속할 수 없습니다.");
    }

    private Long joinUser(final Room room, final User user) {
        User whiteUser = userService.findById(room.whiteUserId());
        if (whiteUser.sameName(user.getName())) {
            whiteUser.checkPassword(user.getPassword());
            return room.getId();
        }
        if (room.haveBlackUser()) {
            User blackUser = userService.findById(room.blackUserId());
            blackUser.checkPassword(user.getPassword());
            return room.getId();
        }
        if (room.isWaitingRoom()) {
            return joinBlackUser(user, room);
        }
        throw new IllegalArgumentException("방에 접속할 수 없습니다.");
    }

    private Long joinBlackUser(final User user, final Room room) {
        Long userId = userService.save(user);
        room.joinBlackUser(userId);
        roomRepository.updateBlackUser(userId, room.getId());
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
        if (room.blackUserId() == null || room.blackUserId().equals(0L)) {
            return new PlayingRoomDto(room.getId(), room.name(), whiteUser.getName(), "");
        }
        User blackUser = userService.findById(room.blackUserId());
        return new PlayingRoomDto(room.getId(), room.name(), whiteUser.getName(), blackUser.getName());
    }

    @Transactional(readOnly = true)
    public RoomDto findById(final Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (!roomOptional.isPresent()) {
            throw new RoomNotFoundException("방을 조회하는데 실패했습니다.");
        }
        Room room = roomOptional.get();
        User whiteUser = userService.findById(room.whiteUserId());
        if (room.blackUserId() == null || room.blackUserId().equals(0L)) {
            return new RoomDto(room.getId(), room.gameId(), room.name(), whiteUser.getName(), "");
        }
        User blackUser = userService.findById(room.blackUserId());
        return new RoomDto(room.getId(), room.gameId(), room.name(), whiteUser.getName(), blackUser.getName());
    }

    @Transactional(readOnly = true)
    public Room findByGameId(final Long gameId) {
        return roomRepository.findByGameId(gameId);
    }
}
