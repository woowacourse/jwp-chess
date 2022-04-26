package chess.web.dao;

import chess.domain.game.state.Player;
import chess.domain.piece.property.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerDaoJdbcImpl implements PlayerDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Color color) {
        final String sql = "insert into player (color) values (?)";
        this.jdbcTemplate.update(
                sql,
                color.name());
    }

    @Override
    public void deleteAll() {
        final String sql = "delete from player";
        this.jdbcTemplate.update(sql);
    }

    @Override
    public Player getPlayer() {
        final String sql = "select color from player";
        String color = jdbcTemplate.queryForObject(sql, String.class);
        System.out.println("dao입니다" + color);
        return Player.of(Color.of(color));
    }
}
