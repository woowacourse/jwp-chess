package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.GameDao;
import wooteco.chess.dao.MoveDao;
import wooteco.chess.dao.PlayerDao;
import wooteco.chess.domain.Game;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;
import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.dto.MoveRequestDto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class SparkChessService implements ChessService {
    private final GameDao gameDao;
    private final MoveDao moveDao;
    private final PlayerDao playerDao;

    public SparkChessService(final GameDao gameDao, final MoveDao moveDao, final PlayerDao playerDao) {
        this.gameDao = gameDao;
        this.moveDao = moveDao;
        this.playerDao = playerDao;
    }

    @Override
    public Game findGameById(final String id) throws SQLException {
        Game game = generateGameFrom(gameDao.findGameDataById(id));
        List<MoveRequestDto> paths = moveDao.getMoves(game);
        paths.forEach(path -> game.move(path.getFrom(), path.getTo()));
        return game;
    }

    private List<Game> generateGames() throws SQLException {
        List<Game> games = new ArrayList<>();
        for (Map<String, String> gameData : gameDao.findGamesData()) {
            games.add(generateGameFrom(gameData));
        }
        return games;
    }

    private Game generateGameFrom(final Map<String, String> gameData) throws SQLException {
        Player white = playerDao.getPlayerById(Integer.parseInt(gameData.get(GameDao.WHITE_ID)));
        Player black = playerDao.getPlayerById(Integer.parseInt(gameData.get(GameDao.BLACK_ID)));
        return new Game(gameData.get(GameDao.GAME_ID), white, black);
    }

    @Override
    public Map<String, Map<Side, Player>> getPlayerContexts() throws SQLException {
        return generateGames()
                .stream()
                .collect(toMap(Game::getId, Game::getPlayers));
    }

    @Override
    public Board findBoardById(String id) throws SQLException {
        return findGameById(id).getBoard();
    }

    @Override
    public Game resetGameById(String id) throws SQLException {
        moveDao.reset(findGameById(id));
        return findGameById(id);
    }

    @Override
    public Map<String, GameResponseDto> addGame(String title, Player white, Player black) throws SQLException {
        HashMap<String, GameResponseDto> result = new HashMap<>();
        Game gameToAdd = new Game(title, white, black);
        String gameId = gameDao.add(gameToAdd);
        result.put(gameId, new GameResponseDto(findGameById(gameId)));
        return result;
    }

    @Override
    public List<String> findAllAvailablePath(String id, String from) throws SQLException {
        return findGameById(id).findAllAvailablePath(from);
    }

    @Override
    public Map<String, Map<Side, Double>> getScoreContexts() throws SQLException {
        Map<String, Map<Side, Double>> result = new HashMap<>();
        for (String gameId : gameDao.getAllGameId()) {
            result.put(gameId, getScoresById(gameId));
        }
        return result;
    }

    @Override
    public Map<Side, Double> getScoresById(final String id) throws SQLException {
        Map<Side, Double> scores = new HashMap<>();
        scores.put(Side.WHITE, getScoreById(id, Side.WHITE));
        scores.put(Side.BLACK, getScoreById(id, Side.BLACK));
        return scores;
    }

    @Override
    public double getScoreById(final String id, final Side side) throws SQLException {
        return findGameById(id).getScoreOf(side);
    }

    @Override
    public boolean isWhiteTurn(final String id) throws SQLException {
        return findGameById(id).isWhiteTurn();
    }

    @Override
    public boolean moveIfMovable(final String id, String start, String end) throws SQLException {
        Path path = findBoardById(id).generatePath(Position.of(start), Position.of(end));
        boolean movable = findGameById(id).move(start, end);
        if (movable) {
            moveDao.addMove(findGameById(id), path);
        }
        return movable;
    }

    @Override
    public boolean finishGameById(final String id) throws SQLException {
        Game game = findGameById(id);
        game.finish();
        playerDao.updatePlayer(game.getPlayer(Side.WHITE));
        playerDao.updatePlayer(game.getPlayer(Side.BLACK));
        moveDao.reset(game);
        gameDao.remove(game);
        return true;
    }

    @Override
    public boolean isGameOver(final String id) throws SQLException {
        return findGameById(id).isGameOver();
    }

    @Override
    public Map<String, GameResponseDto> getBoards() {
        return null;
    }
}
