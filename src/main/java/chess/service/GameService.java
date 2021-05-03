package chess.service;

import chess.domain.game.Game;
import chess.domain.game.GameRepository;
import chess.domain.game.room.Room;
import chess.domain.game.team.Team;
import chess.domain.user.User;
import chess.domain.user.UserRepository;
import chess.exception.GameParticipationFailureException;
import chess.web.dto.game.GameRequestDto;
import chess.web.dto.game.GameResponseDto;
import chess.web.dto.game.join.JoinRequestDto;
import chess.web.dto.game.move.MoveCheckResponseDto;
import chess.web.dto.game.move.MoveRequestDto;
import chess.web.dto.game.move.MoveResponseDto;
import chess.web.dto.game.room.RoomResponseDto;
import chess.web.dto.game.room.RoomsResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public GameService(final GameRepository gameRepository, final UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public long initializeGame(final GameRequestDto gameRequestDto) {
        return gameRepository.add(
            gameRequestDto.getName(),
            gameRequestDto.getHostId()
        );
    }

    public RoomsResponseDto retrieveRoomsData() {
        final List<Room> rooms = gameRepository.findEmptyRooms().toList();
        final List<RoomResponseDto> roomResponseDtos = rooms.stream()
            .map(room -> RoomResponseDto.of(
                room,
                userRepository.findById(room.getHostId()),
                userRepository.findById(room.getGuestId())
            ))
            .collect(Collectors.toList());

        return RoomsResponseDto.from(roomResponseDtos);
    }

    public GameResponseDto retrieveGameData(final long gameId) {
        final Game game = gameRepository.findById(gameId);
        final User host = userRepository.findById(game.getHostId());
        final User guest = userRepository.findById(game.getGuestId());
        return GameResponseDto.of(game, host, guest);
    }

    public void join(final long roomId, final JoinRequestDto joinRequestDto) {
        final long guestId = joinRequestDto.getGuestId();
        final Room room = gameRepository.findRoomById(roomId);
        if (room.isFull()) {
            throw new GameParticipationFailureException("이미 방이 가득 찼습니다.");
        }
        if (room.isAlreadyJoin(guestId)) {
            throw new GameParticipationFailureException("이미 참여한 방입니다.");
        }
        gameRepository.joinGuest(guestId, roomId);
    }

    public MoveCheckResponseDto checkMovement(final long gameId,
        final MoveRequestDto moveRequestDto) {
        final Game game = gameRepository.findById(gameId);
        final boolean isMovable = game.checkMovement(
            moveRequestDto.getSource(), moveRequestDto.getTarget(), moveRequestDto.getColor()
        );
        return new MoveCheckResponseDto(isMovable);
    }

    public MoveResponseDto move(final long gameId, final MoveRequestDto moveRequestDto) {
        final String source = moveRequestDto.getSource();
        final String target = moveRequestDto.getTarget();
        final Team color = moveRequestDto.getColor();
        final Game game = gameRepository.findById(gameId);

        game.move(source, target, color);
        gameRepository.update(game);
        return new MoveResponseDto(source, target, color, game.isFinished());
    }

    public long bringGameIdByRoomId(final long roomId) {
        return gameRepository.findGameIdByRoomId(roomId);
    }

}
