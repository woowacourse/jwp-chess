package chess.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import chess.database.GameStateGenerator;
import chess.database.dao.BoardDao;
import chess.database.dao.GameDao;
import chess.database.dto.BoardDto;
import chess.database.dto.GameStateDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;
import chess.domain.board.Board;
import chess.domain.board.CustomBoardGenerator;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.dto.Arguments;
import chess.dto.RoomRequest;

@Service
public class GameService {

    private final GameDao gameDao;
    private final BoardDao boardDao;
    private final PasswordEncoder encoder;

    public GameService(GameDao gameDao, BoardDao boardDao,
        PasswordEncoder encoder) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
        this.encoder = encoder;
    }

    public Long createNewGame(RoomRequest roomRequest) {
        validateRoomNameDistinct(roomRequest.getRoomName());
        GameState state = new Ready();
        Long gameId = gameDao.saveGame(
            GameStateDto.of(state),
            roomRequest.getRoomName(),
            encoder.encode(roomRequest.getPassword())
        );
        boardDao.saveBoard(BoardDto.of(state.getPointPieces()), gameId);
        return gameId;
    }

    private void validateRoomNameDistinct(String roomName) {
        Optional<GameStateDto> stateAndColor = gameDao.findGameByRoomName(roomName);
        if (stateAndColor.isPresent()) {
            throw new IllegalArgumentException(String.format("[ERROR] %s 이름의 방이 이미 존재합니다.", roomName));
        }
    }

    public void startGame(Long roomId) {
        GameState state = readGameState(roomId).start();
        gameDao.updateState(GameStateDto.of(state), roomId);
    }

    public void finishGame(Long roomId) {
        GameState state = readGameState(roomId).finish();
        gameDao.updateState(GameStateDto.of(state), roomId);
    }

    public GameState readGameState(Long roomId) {
        final GameStateDto gameStateDto = validateExistGame(gameDao.findGameById(roomId), roomId);

        BoardDto boardDto = boardDao.findBoardById(roomId);
        Board board = Board.of(new CustomBoardGenerator(boardDto));

        return GameStateGenerator.generate(board, gameStateDto);
    }

    private GameStateDto validateExistGame(Optional<GameStateDto> gameStateDto, Long roomId) {
        if (gameStateDto.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("[ERROR] %d번에 해당하는 방이 없습니다.", roomId)
            );
        }
        return gameStateDto.get();
    }

    public GameState moveBoard(Long roomId, Arguments arguments) {
        GameState movedState = readGameState(roomId).move(arguments);
        gameDao.updateState(GameStateDto.of(movedState), roomId);

        Route route = Route.of(arguments);
        boardDao.deletePiece(PointDto.of(route.getDestination()), roomId);
        boardDao.updatePiece(RouteDto.of(route), roomId);
        return movedState;
    }

    public void removeGameAndBoard(Long roomId) {
        boardDao.removeBoard(roomId);
        gameDao.removeGame(roomId);
    }

    public Map<Long,String> readGameRooms() {
        return gameDao.readGameRoomIdAndNames();
    }

    public void deleteGame(Long roomId, RoomRequest roomRequest) {
        final String foundPassword = gameDao.findPasswordById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 방이 없습니다."));
        validatePassword(roomRequest.getPassword(), foundPassword);
        boardDao.removeBoard(roomId);
        gameDao.removeGame(roomId);
    }

    private void validatePassword(String password, String foundPassword) {
        if (!encoder.matches(password, foundPassword)) {
            throw new IllegalArgumentException("패스워드가 올바르지 않습니다.");
        }
    }
}
