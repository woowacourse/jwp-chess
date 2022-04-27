package chess.dao;

import chess.domain.piece.Color;
import chess.dto.GameRoomDto;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createByTitleAndPassword(String title, String password) {
        final String sql = "insert into game (title, password) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, title);
            ps.setString(2, password);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean exists(String id) {
        final String sql = "select count(*) from game where id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    public boolean findForceEndFlagById(long id) {
        final String sql = "select force_end_flag from game where id = ?";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public Color findTurnById(long id) {
        final String sql = "select turn from game where id = ?";

        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
            Color.of(resultSet.getString("turn")), id);
    }

    public void updateTurnById(Color nextTurn, long id) {
        final String sql = "update game set turn = ? where id = ?";

        jdbcTemplate.update(sql, nextTurn.getName(), id);
    }

    public void updateForceEndFlagById(boolean forceEndFlag, long id) {
        final String sql = "update game set force_end_flag = ? where id = ?";

        jdbcTemplate.update(sql, forceEndFlag, id);
    }

    public void deleteById(long id) {
        final String sql = "delete from game where id = ?";

        jdbcTemplate.update(sql, id);
    }

    public List<GameRoomDto> findAllIdAndTitle() {
        final String sql = "select id, title from game";

        return jdbcTemplate.query(sql, actorRowMapper);
    }

    private final RowMapper<GameRoomDto> actorRowMapper = (resultSet, rowNum) -> new GameRoomDto(
        resultSet.getLong("id"),
        resultSet.getString("title")
    );

}
