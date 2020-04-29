package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import wooteco.chess.domain.player.Player;
import wooteco.chess.domain.player.Record;
import wooteco.chess.domain.player.Result;

@Component
public class PlayerDao implements MySqlJdbcTemplateDao {

    public void addInitialPlayers() throws SQLException {
        // 플레이어 가입 및 로그인 구현 전 오류 방지를 위한 쿼리
        String query = "INSERT IGNORE INTO player (id, username, password) VALUES (1, 'hodol', 'password'), (2, 'pobi', 'password')";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.executeUpdate();
        }
    }

    public void addPlayer(Player player) throws SQLException {
        String query = "INSERT INTO player (username, password, win, lose, draw) VALUES (?, ?, ?, ?, ?);";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, player.getUsername());
            statement.setString(2, player.getPassword());
            statement.setInt(3, new Record().get(Result.WIN));
            statement.setInt(4, new Record().get(Result.LOSE));
            statement.setInt(5, new Record().get(Result.DRAW));
            statement.executeUpdate();
        }
    }

    public Player getPlayerById(int id) throws SQLException {
        String query = "SELECT * FROM player WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Record record = Record.of(resultSet.getInt("win"), resultSet.getInt("lose"), resultSet.getInt("draw"));
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int playerId = resultSet.getInt("id");
                return new Player(playerId, username, password, record);
            }
            resultSet.close();
            throw new SQLException();
        }
    }

    public void updatePlayer(Player player) throws SQLException {
        String query = "UPDATE player SET win = ?, lose = ?, draw = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, player.recordOf(Result.WIN));
            statement.setInt(2, player.recordOf(Result.LOSE));
            statement.setInt(3, player.recordOf(Result.DRAW));
            statement.setInt(4, player.getId());
            statement.executeUpdate();
        }
    }
}
