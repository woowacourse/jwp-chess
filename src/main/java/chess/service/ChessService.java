package chess.service;

import chess.dao.EventDao;
import chess.dao.GameDao;
import chess.domain.auth.AuthCredentials;
import chess.domain.event.Event;
import chess.domain.event.InitEvent;
import chess.domain.game.Game;
import chess.domain.game.NewGame;
import chess.dto.CreatedGameDto;
import chess.dto.FullGameDto;
import chess.dto.GameCountDto;
import chess.dto.GameOverviewDto;
import chess.dto.GameResultDto;
import chess.dto.GameSnapshotDto;
import chess.dto.SearchResultDto;
import chess.entity.GameEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChessService {

    private static final String GAME_NOT_OVER_EXCEPTION_MESSAGE = "아직 게임 결과가 산출되지 않았습니다.";

    private final GameDao gameDao;
    private final EventDao eventDao;

    public ChessService(GameDao gameDao, EventDao eventDao) {
        this.gameDao = gameDao;
        this.eventDao = eventDao;
    }

    public List<GameOverviewDto> findGames() {
        return gameDao.findAll()
                .stream()
                .map(GameEntity::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public GameCountDto countGames() {
        int totalCount = gameDao.countAll();
        int runningCount = gameDao.countRunningGames();

        return new GameCountDto(totalCount, runningCount);
    }

    @Transactional
    public CreatedGameDto initGame(AuthCredentials authCredentials) {
        int gameId = gameDao.saveAndGetGeneratedId(authCredentials.toEncrypted());
        eventDao.save(gameId, new InitEvent());
        return new CreatedGameDto(gameId);
    }

    public SearchResultDto searchGame(int gameId) {
        return new SearchResultDto(gameId, gameDao.checkById(gameId));
    }

    public FullGameDto findGame(int gameId) {
        GameEntity gameEntity = gameDao.findById(gameId);
        Game game = currentSnapShotOf(gameId);
        return new FullGameDto(gameEntity.toDto(), game.toDtoOf(gameId));
    }

    @Transactional
    public GameSnapshotDto playGame(int gameId, Event moveEvent) {
        Game game = currentSnapShotOf(gameId).play(moveEvent);

        eventDao.save(gameId, moveEvent);
        updateGameState(gameId, game);
        return game.toDtoOf(gameId);
    }

    private void updateGameState(int gameId, Game game) {
        if (game.isEnd()) {
            gameDao.finishGame(gameId);
        }
    }

    public GameResultDto findGameResult(int gameId) {
        GameEntity gameEntity = gameDao.findById(gameId);
        Game game = currentSnapShotOf(gameId);
        validateGameOver(game);
        return new GameResultDto(
                new FullGameDto(gameEntity.toDto(), game.toDtoOf(gameId)),
                game.getResult());
    }

    private Game currentSnapShotOf(int gameId) {
        List<Event> events = eventDao.findAllByGameId(gameId);
        Game game = new NewGame();
        for (Event event : events) {
            game = game.play(event);
        }
        return game;
    }

    private void validateGameOver(Game game) {
        if (!game.isEnd()) {
            throw new IllegalArgumentException(GAME_NOT_OVER_EXCEPTION_MESSAGE);
        }
    }
}
