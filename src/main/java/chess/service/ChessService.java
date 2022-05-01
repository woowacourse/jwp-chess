package chess.service;

import chess.dao.EventDao;
import chess.dao.GameDao;
import chess.domain.event.Event;
import chess.domain.event.InitEvent;
import chess.domain.event.MoveEvent;
import chess.domain.game.Game;
import chess.domain.game.NewGame;
import chess.dto.CreateGameRequest;
import chess.dto.CreateGameResponse;
import chess.dto.DeleteGameRequest;
import chess.dto.GameCountDto;
import chess.dto.GameDto;
import chess.dto.GameResultDto;
import chess.dto.MoveRouteDto;
import chess.entity.EventEntity;
import chess.entity.GameEntity;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private static final String GAME_NOT_OVER_EXCEPTION_MESSAGE = "아직 게임이 종료되지 않았습니다.";
    private static final String PASSWORD_EXCEPTION_MESSAGE = "비밀번호가 일치하지 않습니다.";
    private static final String DELETE_FAILED_EXCEPTION_MESSAGE = "삭제가 되지 않았습니다.";

    private final GameDao gameDao;
    private final EventDao eventDao;

    public ChessService(GameDao gameDao, EventDao eventDao) {
        this.gameDao = gameDao;
        this.eventDao = eventDao;
    }

    @Transactional(readOnly = true)
    public GameCountDto countGames() {
        int totalCount = gameDao.countAll();
        int runningCount = gameDao.countRunningGames();

        return new GameCountDto(totalCount, runningCount);
    }

    @Transactional
    public CreateGameResponse initGame(CreateGameRequest createGameRequest) {
        int gameId = gameDao.saveAndGetGeneratedId(createGameRequest);
        EventEntity event = EventEntity.of(new InitEvent());

        eventDao.save(gameId, event);
        return new CreateGameResponse(gameId);
    }

    @Transactional(readOnly = true)
    public GameDto findGame(int gameId) {
        Game game = currentSnapShotOf(gameId);
        return GameDto.of(gameId, game);
    }

    @Transactional
    public GameDto playGame(int gameId, MoveRouteDto moveRoute) {
        Event moveEvent = new MoveEvent(moveRoute.toMoveRoute());
        Game game = currentSnapShotOf(gameId).play(moveEvent);

        eventDao.save(gameId, EventEntity.of(moveEvent));
        updateGameState(gameId, game);
        return GameDto.of(gameId, game);
    }

    private void updateGameState(int gameId, Game game) {
        if (game.isEnd()) {
            gameDao.finishGame(gameId);
        }
    }

    @Transactional(readOnly = true)
    public GameResultDto findGameResult(int gameId) {
        Game game = currentSnapShotOf(gameId);
        validateGameOver(game);
        return new GameResultDto(gameId, game);
    }

    private Game currentSnapShotOf(int gameId) {
        List<EventEntity> events = eventDao.findAllByGameId(gameId);
        Game game = new NewGame();
        for (EventEntity event : events) {
            game = game.play(Event.of(event));
        }
        return game;
    }

    private void validateGameOver(Game game) {
        if (!game.isEnd()) {
            throw new IllegalArgumentException(GAME_NOT_OVER_EXCEPTION_MESSAGE);
        }
    }

    @Transactional(readOnly = true)
    public List<GameEntity> selectAllGames() {
        return gameDao.selectAll();
    }

    @Transactional
    public void deleteGame(int id, DeleteGameRequest deleteGameRequest) {
        GameEntity gameEntity = gameDao.findById(id);

        if (gameEntity.isRunning()) {
            throw new IllegalArgumentException(GAME_NOT_OVER_EXCEPTION_MESSAGE);
        }

        if (!gameEntity.getPassword().equals(deleteGameRequest.getPassword())) {
            throw new IllegalArgumentException(PASSWORD_EXCEPTION_MESSAGE);
        }

        int result = gameDao.delete(id);
        if (result == 0) {
            throw new IllegalArgumentException(DELETE_FAILED_EXCEPTION_MESSAGE);
        }
    }
}
