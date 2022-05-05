package chess.service;

import chess.database.GameStateGenerator;
import chess.database.dao.BoardDao;
import chess.database.dao.GameDao;
import chess.database.dao.RoomDao;
import chess.database.dto.GameStateDto;
import chess.database.dto.RoomDto;
import chess.database.dto.RouteDto;
import chess.domain.Room;
import chess.domain.board.Board;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.domain.game.State;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChessRoomService {

    private final RoomDao roomDao;
    private final GameDao gameDao;
    private final BoardDao boardDao;

    public ChessRoomService(RoomDao roomDao, GameDao gameDao, BoardDao boardDao) {
        this.roomDao = roomDao;
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public RoomDto createNewRoom(String roomName, String password) {
        Room room = new Room(roomName, password);
        RoomDto roomDto = new RoomDto(roomName, password);
        validateDuplicateRoom(roomName);
        roomDto = roomDao.create(roomDto);
        GameState state = new Ready();
        gameDao.create(roomDto.getId(), state);

        boardDao.saveBoard(roomDto.getId(), state.getBoard());
        return roomDto;
    }

    public void startGame(int roomId) {
        GameState state = readGameState(roomId).start();
        gameDao.updateState(roomId, GameStateDto.of(state));
    }

    public void finishGame(int roomId) {
        GameState state = readGameState(roomId).finish();
        gameDao.updateState(roomId, GameStateDto.of(state));
    }

    public GameState readGameState(int roomId) {
        GameStateDto gameStateDto = gameDao.readStateAndColor(roomId);
        Board board = boardDao.readBoard(roomId);
        return GameStateGenerator.generate(board, gameStateDto);
    }

    public GameState moveBoard(int roomId, RouteDto routeDto) {
        GameState movedState = readGameState(roomId).move(routeDto);
        gameDao.updateState(roomId, GameStateDto.of(movedState));
        Route route = Route.of(routeDto);
        boardDao.deletePiece(roomId, route.getDestination());
        boardDao.updatePiece(roomId, route.getSource(), route.getDestination());
        return movedState;
    }

    public void removeRoom(RoomDto roomDto) {
        validateExistRoom(roomDto.getId());
        RoomDto findRoomDto = roomDao.findById(roomDto.getId());
        Room room = new Room(findRoomDto.getName(), findRoomDto.getPassword());
        room.validatePassword(roomDto.getPassword());
        GameStateDto gameStateDto = gameDao.readStateAndColor(findRoomDto.getId());
        validateRunningState(gameStateDto.getState());
        boardDao.removeBoard(findRoomDto.getId());
        gameDao.removeGame(findRoomDto.getId());
        roomDao.delete(findRoomDto.getId());
    }


    public Map<RoomDto, String> findAllRoomState() {
        Map<RoomDto, String> roomStates = new HashMap<>();
        List<RoomDto> roomDtoList = roomDao.findAll();
        for (RoomDto roomDto : roomDtoList) {
            GameStateDto gameStateDto = gameDao.readStateAndColor(roomDto.getId());
            State state = gameStateDto.getState();
            roomStates.put(roomDto, state.getDisableOption());
        }
        return roomStates;
    }

    public RoomDto findById(int roomId) {
        return roomDao.findById(roomId);
    }

    private void validateDuplicateRoom(String roomName) {
        if (roomDao.existRoomName(roomName)) {
            throw new IllegalArgumentException("[ERROR] 이름의 방이 이미 존재합니다.");
        }
    }

    private void validateExistRoom(int roomId) {
        if (!roomDao.existRoomId(roomId)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 방입니다.");
        }
    }

    private void validateRunningState(State state) {
        if (state == State.RUNNING) {
            throw new IllegalArgumentException("[ERROR] 진행 중인 게임은 삭제할 수 없습니다.");
        }
    }
}
