package chess.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import chess.domain.Room;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.dto.Arguments;
import chess.dto.RoomRequest;
import chess.repository.GameRepository;
import chess.repository.RoomRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder encoder;

    public GameService(GameRepository gameRepository,
        RoomRepository roomRepository,
        PasswordEncoder encoder) {
        this.gameRepository = gameRepository;
        this.roomRepository = roomRepository;
        this.encoder = encoder;
    }

    public Long createNewGame(RoomRequest roomRequest) {
        Room room = Room.createRoomEncoded(roomRequest, encoder);
        return roomRepository.createRoom(room, new Ready());
    }

    public void startGame(Long gameId) {
        GameState state = gameRepository.findGameById(gameId).start();
        gameRepository.updateState(state, gameId);
    }

    public void finishGame(Long gameId) {
        GameState state = gameRepository.findGameById(gameId).finish();
        gameRepository.updateState(state, gameId);
    }

    public GameState readGameState(Long gameId) {
        return gameRepository.findGameById(gameId);
    }

    public GameState moveBoard(Long gameId, Arguments arguments) {
        Route route = Route.of(arguments);
        final GameState moved = gameRepository.findGameById(gameId).move(arguments); // TODO: arguments to route
        gameRepository.updateState(moved, route, gameId);
        return moved;
    }

    public void removeRoom(Long roomId, RoomRequest roomRequest) {
        validatePasswordMatches(roomId, roomRequest);
        validateGameNotRunning(roomId);
        roomRepository.deleteRoom(roomId);
    }

    private void validatePasswordMatches(Long roomId, RoomRequest roomRequest) {
        final Room room = roomRepository.findRoomById(roomId);
        if (!room.isPasswordMatches(roomRequest.getPassword(), encoder)) {
            throw new IllegalArgumentException("[ERROR] 패스워드가 올바르지 않습니다.");
        }
    }

    private void validateGameNotRunning(Long roomId) {
        final GameState state = gameRepository.findGameByRoomId(roomId);
        if (state.isRunnable()) {
            throw new IllegalStateException("[ERROR] 진행중인 게임은 삭제할 수 없습니다.");
        }
    }

    public List<Room> readGameRooms() {
        return roomRepository.findAllRooms();
    }
}
