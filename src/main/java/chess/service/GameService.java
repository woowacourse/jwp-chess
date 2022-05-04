package chess.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import chess.domain.Room;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.dto.RoomRequest;
import chess.dto.RouteRequest;
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

    public Long findGameIdByRoomId(Long roomId) {
        return gameRepository.findGameIdByRoomId(roomId);
    }

    public GameState moveBoard(Long gameId, RouteRequest routeRequest) {
        final Route route = Route.of(routeRequest.getSource(), routeRequest.getDestination());
        final GameState moved = gameRepository.findGameById(gameId).move(route);
        gameRepository.updateState(moved, route, gameId);
        return moved;
    }

    public GameState readGameState(Long gameId) {
        return gameRepository.findGameById(gameId);
    }

    public void startGame(Long gameId) {
        final GameState started = gameRepository.findGameById(gameId).start();
        gameRepository.updateState(started, gameId);
    }

    public void finishGame(Long gameId) {
        final GameState finished = gameRepository.findGameById(gameId).finish();
        gameRepository.updateState(finished, gameId);
    }
}
