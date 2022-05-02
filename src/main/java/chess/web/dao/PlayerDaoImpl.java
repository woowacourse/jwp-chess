package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import chess.web.jdbc.JdbcTemplate;
import chess.web.jdbc.SelectJdbcTemplate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDaoImpl implements PlayerDao {

    @Override
    public void save(Color color) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                statement.setString(1, color.name());
            }
        };
        final String sql = "insert into player (color) values (?)";
        jdbcTemplate.executeUpdate(sql);
    }

    @Override
    public void saveById(int id, Color of) {
    }

    @Override
    public Player getPlayer() {
        SelectJdbcTemplate jdbcTemplate = new SelectJdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                return;
            }

            @Override
            public Object mapRow(ResultSet resultSet) throws SQLException {
                Player player = null;
                if (resultSet.next()) {
                    Color color = Color.of(resultSet.getString("color"));
                    player = Player.of(color);
                }
                return player;
            }
        };
        final String sql = "select color from player";
        return (Player) jdbcTemplate.executeQuery(sql);
    }

    @Override
    public void deleteAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate() {
            @Override
            public void setParameters(PreparedStatement statement) throws SQLException {
                return;
            }
        };
        final String sql = "delete from player";
        jdbcTemplate.executeUpdate(sql);
    }

    @Override
    public Player findById(int id) {
        return null;
    }

    @Override
    public void deleteById(int id) {
    }
}
