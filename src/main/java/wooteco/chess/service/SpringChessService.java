package wooteco.chess.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import wooteco.chess.domain.Game;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;
import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.entity.GameEntity;
import wooteco.chess.entity.MoveEntity;
import wooteco.chess.entity.PlayerEntity;
import wooteco.chess.exceptions.GameNotFoundException;
import wooteco.chess.exceptions.PlayerNotFoundException;
import wooteco.chess.repository.GameRepository;
import wooteco.chess.repository.MoveRepository;
import wooteco.chess.repository.PlayerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Primary
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
    public Map<String, GameResponseDto> getGames() {
        return generateGames()
                .stream()
                .collect(toMap(Game::getId, GameResponseDto::new));
    }

    @Override
    public Map<String, GameResponseDto> addGame(final String title, final Player white, final Player black) {
        HashMap<String, GameResponseDto> result = new HashMap<>();
        GameEntity newGame = gameRepository.save(new GameEntity(new Game(title, white, black)));
        // TODO: 플레어어 db 에서 가져오기
        result.put(newGame.getId(), new GameResponseDto(newGame.toModel(white, black)));
        return result;
    }

    @Override
    public Game findGameById(final String id) {
        GameEntity gameEntity = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
        Player white = findPlayerById(gameEntity.getWhiteId());
        Player black = findPlayerById(gameEntity.getBlackId());
        Game game = gameEntity.toModel(white, black);
        return movesRecoveredGame(id, game);
    }

    private Game movesRecoveredGame(final String id, final Game game) {
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
    public Board findBoardById(final String id) {
        return findGameById(id).getBoard();
    }

    @Override
    public Game resetGameById(final String id) {
        moveRepository.deleteAllByGameId(id);
        return findGameById(id);
    }

    @Override
    public boolean finishGameById(final String id) {
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

    private List<Game> generateGames() {
        return gameRepository.findAll()
                .stream()
                .map(gameEntity -> gameEntity.toModel(
                        findPlayerById(gameEntity.getWhiteId()),
                        findPlayerById(gameEntity.getBlackId()))
                )
                .collect(toList());
    }

    @Override
    public Map<String, Map<Side, Double>> getScoreContexts() {
        return gameRepository.findAll()
                .stream()
                .map(GameEntity::getId)
                .collect(toMap(Function.identity(), this::getScoresById));
    }

    @Override
    public Map<Side, Double> getScoresById(final String id) {
        Map<Side, Double> scores = new HashMap<>();
        scores.put(Side.WHITE, getScoreById(id, Side.WHITE));
        scores.put(Side.BLACK, getScoreById(id, Side.BLACK));
        return scores;
    }

    public double getScoreById(final String id, final Side side) {
        return findGameById(id).getScoreOf(side);
    }

    @Override
    public boolean moveIfMovable(final String gameId, final String start, final String end) {
        boolean movable = findGameById(gameId).move(start, end);
        if (movable) {
            moveRepository.save(new MoveEntity(gameId, start, end));
        }
        return movable;
    }

    @Override
    public List<String> findAllAvailablePath(final String id, final String start) {
        return findGameById(id).findAllAvailablePath(start);
    }

    @Override
    public boolean isWhiteTurn(final String id) {
        return findGameById(id).isWhiteTurn();
    }

    @Override
    public boolean isGameOver(final String id) {
        return findGameById(id).isGameOver();
    }
}
