package chess.service;

import chess.database.GameStateGenerator;
import chess.database.dao.BoardDao;
import chess.database.dao.GameDao;
import chess.database.dao.spring.RoomDao;
import chess.database.dto.BoardDto;
import chess.database.dto.GameStateDto;
import chess.database.dto.PointDto;
import chess.database.dto.RoomDto;
import chess.database.dto.RouteDto;
import chess.domain.board.Board;
import chess.domain.board.CustomBoardGenerator;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.domain.game.State;
import chess.dto.Arguments;
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
        RoomDto roomDto = new RoomDto(roomName, password);
        validateDuplicateRoomName(roomName);
        roomDto = roomDao.create(roomDto);
        GameState state = new Ready();
        gameDao.create(GameStateDto.of(state), roomDto.getId());
        boardDao.saveBoard(BoardDto.of(state.getPointPieces()), roomDto.getId());
        return roomDto;
    }

    public void startGame(int roomId) {
        GameState state = readGameState(roomId).start();
        gameDao.updateState(GameStateDto.of(state), roomId);
    }

    public void finishGame(int roomId) {
        GameState state = readGameState(roomId).finish();
        gameDao.updateState(GameStateDto.of(state), roomId);
    }

    public GameState readGameState(int roomId) {
        GameStateDto gameStateDto = gameDao.readStateAndColor(roomId);
        validateExistGame(gameStateDto, roomId);
        BoardDto boardDto = boardDao.readBoard(roomId);
        validateExistRoom(roomId, boardDto);
        Board board = Board.of(new CustomBoardGenerator(boardDto));
        return GameStateGenerator.generate(board, gameStateDto);
    }

    public GameState moveBoard(int roomId, Arguments arguments) {
        GameState movedState = readGameState(roomId).move(arguments);
        gameDao.updateState(GameStateDto.of(movedState), roomId);
        Route route = Route.of(arguments);
        boardDao.deletePiece(PointDto.of(route.getDestination()), roomId);
        boardDao.updatePiece(RouteDto.of(route), roomId);
        return movedState;
    }

    public void removeRoom(int roomId) {
        RoomDto roomDto = roomDao.findById(roomId);
        validateRoomName(roomDto);
        boardDao.removeBoard(roomId);
        gameDao.removeGame(roomId);
        roomDao.delete(roomDto);
    }

    public void removeRoom(RoomDto roomDto) {
//        RoomDto findRoomDto = roomDao.findByName(roomDto.getName());
        RoomDto findRoomDto = roomDao.findById(roomDto.getId());
//        validateRoomName(findRoomDto);
        GameStateDto gameStateDto = gameDao.readStateAndColor(findRoomDto.getId());
        validateRunningState(gameStateDto.getState());
        validatePassword(roomDto, findRoomDto);
        boardDao.removeBoard(findRoomDto.getId());
        gameDao.removeGame(findRoomDto.getId());
        roomDao.delete(findRoomDto.getId());
    }

//    public Map<String, String> findAllRoomState() {
    public Map<RoomDto, String> findAllRoomState() {
//        Map<String, String> roomStates = new HashMap<>();
        Map<RoomDto, String> roomStates = new HashMap<>();
        List<RoomDto> roomDtoList = roomDao.findAll();
        for (RoomDto roomDto : roomDtoList) {
            GameStateDto gameStateDto = gameDao.readStateAndColor(roomDto.getId());
            State state = gameStateDto.getState();
            roomStates.put(roomDto, state.getDisableOption());
        }
        return roomStates;
    }

    public RoomDto findByName(String roomName) {
        return roomDao.findByName(roomName);
    }

    public RoomDto findById(int roomId) {
        return roomDao.findById(roomId);
    }

    private void validateExistGame(GameStateDto stateAndColor, int roomId) {
        if (stateAndColor == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 방입니다.");
        }
    }

    private void validateExistRoom(int roomId, BoardDto boardDto) {
        if (boardDto.getPointPieces().size() == 0) {
            throw new IllegalArgumentException(String.format("[ERROR] %s에 해당하는 번호의 보드가 없습니다.",
                roomId));
        }
    }

    private void validateDuplicateRoomName(String roomName) {
        if (roomDao.findByName(roomName) != null) {
            throw new IllegalArgumentException(String.format("[ERROR] %s 이름의 방이 이미 존재합니다.",
                roomName));
        }
    }

    private void validateRoomName(RoomDto roomDto) {
        if (roomDto == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 방 이름입니다.");
        }
    }

    private void validateRunningState(State state) {
        if (state == State.RUNNING) {
            throw new IllegalArgumentException("[ERROR] 진행 중인 게임은 삭제할 수 없습니다.");
        }
    }

    private void validatePassword(RoomDto roomDto, RoomDto findRoomDto) {
        if (!findRoomDto.getPassword().equals(roomDto.getPassword())) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않습니다.");
        }
    }
}
