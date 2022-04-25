package chess.service;

import java.util.List;

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

@Service
public class GameService {

    private final GameDao gameDao;
    private final BoardDao boardDao;

    public GameService(GameDao gameDao, BoardDao boardDao) {
        this.gameDao = gameDao;
        this.boardDao = boardDao;
    }

    public void createNewGame(String roomName) {
        validateDistinctGame(roomName);
        GameState state = new Ready();
        gameDao.saveGame(GameStateDto.of(state), roomName);
        boardDao.saveBoard(BoardDto.of(state.getPointPieces()), roomName);
    }

    private void validateDistinctGame(String roomName) {
        List<String> stateAndColor = gameDao.readStateAndColor(roomName);
        if (!stateAndColor.isEmpty()) {
            throw new IllegalArgumentException(String.format("[ERROR] %s 이름의 방이 이미 존재합니다.", roomName));
        }
    }

    public void startGame(String roomName) {
        GameState state = readGameState(roomName).start();
        gameDao.updateState(GameStateDto.of(state), roomName);
    }

    public void finishGame(String roomName) {
        GameState state = readGameState(roomName).finish();
        gameDao.updateState(GameStateDto.of(state), roomName);
    }

    public GameState readGameState(String roomName) {
        List<String> stateAndColor = gameDao.readStateAndColor(roomName);
        validateExistGame(stateAndColor, roomName);

        BoardDto boardDto = boardDao.readBoard(roomName);
        Board board = Board.of(new CustomBoardGenerator(boardDto));
        return GameStateGenerator.generate(board, stateAndColor);
    }

    private void validateExistGame(List<String> stateAndColor, String roomName) {
        if (stateAndColor.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("[ERROR] %s 이름에 해당하는 방이 없습니다.", roomName)
            );
        }
    }

    public GameState moveBoard(String roomName, Arguments arguments) {
        GameState movedState = readGameState(roomName).move(arguments);
        gameDao.updateState(GameStateDto.of(movedState), roomName);

        Route route = Route.of(arguments);
        boardDao.deletePiece(PointDto.of(route.getDestination()), roomName);
        boardDao.updatePiece(RouteDto.of(route), roomName);
        return movedState;
    }

    public void removeGameAndBoard(String roomName) {
        boardDao.removeBoard(roomName);
        gameDao.removeGame(roomName);
    }
}
