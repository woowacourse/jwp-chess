package chess.dao;

import chess.entity.GameEntity;
import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("GameDbDao")
public class GameDbDao implements GameDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GameEntity> gameEntityRowMapper = (resultSet, rowNum) -> GameEntity.of(
            resultSet.getInt("game_id"),
            resultSet.getString("current_turn")
    );

    public GameDbDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void updateById(GameEntity gameEntity) {
        String sql = "UPDATE game SET current_turn=? WHERE game_id=?";
        jdbcTemplate.update(sql, gameEntity.getTurn(), gameEntity.getGameId());
    }

    @Override
    public GameEntity findById(GameEntity gameEntity) {
        String sql = "select * from game where game_id = ?";
        return jdbcTemplate.queryForObject(sql, gameEntityRowMapper, gameEntity.getGameId());
    }

    @Override
    public void insert(GameEntity gameEntity) {
        String sql = "insert into game (current_turn) values (?)";
        jdbcTemplate.update(sql, gameEntity.getTurn());
    }

    @Override
    public void deleteById(GameEntity gameEntity) {
        String sql = "delete from game where game_id = ?";
        jdbcTemplate.update(sql, gameEntity.getGameId());
    }

    @Override
    public int insertWithKeyHolder(GameEntity gameEntity) {
        String sql = "insert into game (current_turn) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"game_id"});
            ps.setString(1, gameEntity.getTurn());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
}
