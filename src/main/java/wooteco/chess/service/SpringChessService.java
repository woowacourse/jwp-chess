package wooteco.chess.service;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import wooteco.chess.domain.Game;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;
import wooteco.chess.entity.GameEntity;
import wooteco.chess.entity.MoveEntity;
import wooteco.chess.entity.PlayerEntity;
import wooteco.chess.exceptions.GameNotFoundException;
import wooteco.chess.exceptions.PlayerNotFoundException;
import wooteco.chess.repository.GameRepository;
import wooteco.chess.repository.MoveRepository;
import wooteco.chess.repository.PlayerRepository;

@Service
public class SpringChessService implements ChessService {

    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;
    private final PlayerRepository playerRepository;

    public SpringChessService(GameRepository gameRepository, MoveRepository moveRepository,
        PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public Map<Integer, Map<Side, Player>> addGame(final Player white, final Player black) {
        HashMap<Integer, Map<Side, Player>> result = new HashMap<>();
        GameEntity newGame = gameRepository.save(new GameEntity(new Game(white, black)));
        // TODO: 플레어어 db 에서 가져오기
        result.put(newGame.getId(), newGame.toModel(white, black).getPlayers());
        return result;
    }

    @Override
    public Game findGameById(final int id) {
        GameEntity gameEntity = gameRepository.findById(id)
            .orElseThrow(() -> new GameNotFoundException(id));
        Player white = findPlayerById(gameEntity.getWhiteId());
        Player black = findPlayerById(gameEntity.getBlackId());
        Game game = gameEntity.toModel(white, black);
        return movesRecoveredGame(id, game);
    }

    private Game movesRecoveredGame(final int id, final Game game) {
        moveRepository.findAllByGameId(id)
            .forEach(move -> game.move(move.getStart(), move.getEnd()));
        return game;
    }

    private Player findPlayerById(final int id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new PlayerNotFoundException(id))
            .toModel();
    }

    @Override
    public Board findBoardById(final int id) {
        return findGameById(id).getBoard();
    }

    @Override
    public Board resetGameById(final int id) {
        moveRepository.deleteAllByGameId(id);
        return findBoardById(id);
    }

    @Override
    public boolean finishGameById(final int id) {
        Game game = findGameById(id);
        game.finish();
        saveGameResultToPlayers(game);
        resetGameById(id);
        gameRepository.deleteById(id);
        return true;
    }

    private void saveGameResultToPlayers(final Game game) {
        playerRepository.save(new PlayerEntity(game.getPlayer(Side.WHITE)));
        playerRepository.save(new PlayerEntity(game.getPlayer(Side.BLACK)));
    }

    @Override
    public double getScoreById(final int id, final Side side) {
        return findGameById(id).getScoreOf(side);
    }

    @Override
    public Map<Integer, Map<Side, Player>> getPlayerContexts() {
        return generateGames()
            .stream()
            .collect(toMap(Game::getId, Game::getPlayers));
    }

    private List<Game> generateGames() {
        List<GameEntity> games = Lists.newArrayList(gameRepository.findAll());
        return games.stream()
            .map(gameEntity -> gameEntity.toModel(findPlayerById(gameEntity.getWhiteId()),
                findPlayerById(gameEntity.getBlackId())))
            .collect(toList());
    }

    @Override
    public Map<Integer, Map<Side, Double>> getScoreContexts() {
        List<GameEntity> games = new ArrayList<>();
        gameRepository.findAll().forEach(games::add);
        return games.stream()
            .map(GameEntity::getId)
            .collect(toMap(Function.identity(), this::getScoresById));
    }

    @Override
    public Map<Side, Double> getScoresById(final int id) {
        Map<Side, Double> scores = new HashMap<>();
        scores.put(Side.WHITE, getScoreById(id, Side.WHITE));
        scores.put(Side.BLACK, getScoreById(id, Side.BLACK));
        return scores;
    }

    @Override
    public boolean moveIfMovable(final int gameId, final String start, final String end) {
        boolean movable = findGameById(gameId).move(start, end);
        if (movable) {
            moveRepository.save(new MoveEntity(gameId, start, end));
        }
        return movable;
    }

    @Override
    public List<String> findAllAvailablePath(final int id, final String start) {
        return findGameById(id).findAllAvailablePath(start);
    }

    @Override
    public boolean isWhiteTurn(final int id) {
        return findGameById(id).isWhiteTurn();
    }

    @Override
    public boolean isGameOver(final int id) {
        return findGameById(id).isGameOver();
    }
}
