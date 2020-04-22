package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.piece.Side;

public class GameDao implements MySqlJdbcTemplateDao {

    public static final String GAME_ID = "id";
    public static final String WHITE_ID = "white";
    public static final String BLACK_ID = "black";

    public int add(final Game game) throws SQLException {
        String query = "insert into game (white, black) values (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setInt(1, game.getPlayerId(Side.WHITE));
            statement.setInt(2, game.getPlayerId(Side.BLACK));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            resultSet.close();
            throw new SQLException();
        }
    }

    public List<Integer> getAllGameId() throws SQLException {
        String query = "select id from game";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet = statement.executeQuery();
            List<Integer> gameIds = new ArrayList<>();
            addGameIds(resultSet, gameIds);
            resultSet.close();
            return gameIds;
        }
    }

    private void addGameIds(final ResultSet resultSet, final List<Integer> gameIds) throws SQLException {
        while (resultSet.next()) {
            gameIds.add(resultSet.getInt("id"));
        }
    }

    public List<Map<String, Integer>> findGamesData() throws SQLException {
        String query = "select id, white, black from game";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet = statement.executeQuery();
            List<Map<String, Integer>> games = new ArrayList<>();
            generateGamesContext(resultSet, games);
            resultSet.close();
            return games;
        }
    }

    private void generateGamesContext(final ResultSet resultSet, final List<Map<String, Integer>> games) throws
        SQLException {
        while (resultSet.next()) {
            Map<String, Integer> game = new HashMap<>();
            game.put(GAME_ID, resultSet.getInt("id"));
            game.put(WHITE_ID, resultSet.getInt("white"));
            game.put(BLACK_ID, resultSet.getInt("black"));
            games.add(game);
        }
    }

    public Map<String, Integer> findGameDataById(int gameId) throws SQLException {
        String query = "select id, white, black from game where id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();
            Map<String, Integer> game = new HashMap<>();
            if (resultSet.next()) {
                game.put(GAME_ID, resultSet.getInt("id"));
                game.put(WHITE_ID, resultSet.getInt("white"));
                game.put(BLACK_ID, resultSet.getInt("black"));
            }
            resultSet.close();
            return game;
        }
    }

    public void remove(final Game game) throws SQLException {
        String query = "delete from game where id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, game.getId());
            statement.executeUpdate();
        }
    }
}
