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
import chess.dto.Arguments;
import java.util.List;
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
        if (roomDao.findByName(roomName) != null) {
            throw new IllegalArgumentException(
                String.format("[ERROR] %s 이름의 방이 이미 존재합니다.", roomName));
        }
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
        List<String> stateAndColor = gameDao.readStateAndColor(roomId);
        validateExistGame(stateAndColor, roomId);

        BoardDto boardDto = boardDao.readBoard(roomId);
        Board board = Board.of(new CustomBoardGenerator(boardDto));
        return GameStateGenerator.generate(board, stateAndColor);
    }

    private void validateExistGame(List<String> stateAndColor, int roomId) {
        if (stateAndColor.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("[ERROR] %s 이름에 해당하는 방이 없습니다.", roomId));
        }
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
        boardDao.removeBoard(roomId);
        gameDao.removeGame(roomId);
        roomDao.delete(roomDto);
    }

    public RoomDto findByName(String roomName) {
        return roomDao.findByName(roomName);
    }

    public void removeRoom(RoomDto roomDto) {
        RoomDto findRoomDto = roomDao.findByName(roomDto.getName());
        if (findRoomDto.getPassword() != null) {
            if (!findRoomDto.getPassword().equals(roomDto.getPassword())) {
                throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않습니다.");
            }
        }
        boardDao.removeBoard(roomDto.getId());
        gameDao.removeGame(roomDto.getId());
        roomDao.delete(roomDto);
    }

    public List<RoomDto> findAllRoom() {
        return roomDao.findAll();
    }
}
