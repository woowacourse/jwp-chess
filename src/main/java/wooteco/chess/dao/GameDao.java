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

import org.springframework.stereotype.Component;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.piece.Side;

@Component
public class GameDao implements MySqlJdbcTemplateDao {

    public static final String GAME_ID = "id";
    public static final String WHITE_ID = "white";
    public static final String BLACK_ID = "black";

    public String add(final Game game) throws SQLException {
        String query = "INSERT INTO game (id, white, black) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, game.getId());
            statement.setInt(2, game.getPlayerId(Side.WHITE));
            statement.setInt(3, game.getPlayerId(Side.BLACK));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getString(Statement.RETURN_GENERATED_KEYS);
            }
            resultSet.close();
            throw new SQLException();
        }
    }

    public List<String> getAllGameId() throws SQLException {
        String query = "SELECT id FROM game";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet = statement.executeQuery();
            List<String> gameIds = new ArrayList<>();
            addGameIds(resultSet, gameIds);
            resultSet.close();
            return gameIds;
        }
    }

    private void addGameIds(final ResultSet resultSet, final List<String> gameIds) throws SQLException {
        while (resultSet.next()) {
            gameIds.add(resultSet.getString("id"));
        }
    }

    public List<Map<String, String>> findGamesData() throws SQLException {
        String query = "SELECT id, white, black FROM game";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet = statement.executeQuery();
            List<Map<String, String>> games = new ArrayList<>();
            generateGamesContext(resultSet, games);
            resultSet.close();
            return games;
        }
    }

    private void generateGamesContext(final ResultSet resultSet, final List<Map<String, String>> games) throws
        SQLException {
        while (resultSet.next()) {
            Map<String, String> game = new HashMap<>();
            game.put(GAME_ID, resultSet.getString("id"));
            game.put(WHITE_ID, Integer.toString(resultSet.getInt("white")));
            game.put(BLACK_ID, Integer.toString(resultSet.getInt("black")));
            games.add(game);
        }
    }
    
    public Map<String, String> findGameDataById(String gameId) throws SQLException {
        String query = "SELECT id, white, black FROM game WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, gameId);
            ResultSet resultSet = statement.executeQuery();
            Map<String, String> game = new HashMap<>();
            if (resultSet.next()) {
                game.put(GAME_ID, resultSet.getString("id"));
                game.put(WHITE_ID, Integer.toString(resultSet.getInt("white")));
                game.put(BLACK_ID, Integer.toString(resultSet.getInt("black")));
            }
            resultSet.close();
            return game;
        }
    }

    public void remove(final Game game) throws SQLException {
        String query = "DELETE FROM game WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, game.getId());
            statement.executeUpdate();
        }
    }
}
