package chess.dao.jdbctemplate;

import chess.dao.ScoreDao;
import chess.dao.dto.score.ScoreDto;
import chess.domain.manager.GameStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class ScoreDaoTemplate implements ScoreDao {

    private final JdbcTemplate jdbcTemplate;

    public ScoreDaoTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ScoreDto> actorRowMapper = (resultSet, rowNum) ->
            new ScoreDto(
                    resultSet.getLong("id"),
                    resultSet.getLong("game_id"),
                    resultSet.getDouble("white_score"),
                    resultSet.getDouble("black_score")
            );

    @Override
    public Long save(final ScoreDto scoreDto) {
        String sql = "INSERT INTO score(game_id, white_score, black_score) VALUES (?, ?, ?)";
        return (long) jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, scoreDto.getGameId());
            ps.setDouble(2, scoreDto.getWhiteScore());
            ps.setDouble(3, scoreDto.getBlackScore());
            return ps;
        }, new GeneratedKeyHolder());
    }

    @Override
    public Long update(final ScoreDto scoreDto) {
        String sql = "UPDATE score SET white_score=?, black_score=? WHERE game_id=?";
        return (long) jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setDouble(1, scoreDto.getWhiteScore());
            ps.setDouble(2, scoreDto.getBlackScore());
            ps.setLong(3, scoreDto.getGameId());
            return ps;
        }, new GeneratedKeyHolder());
    }

    @Override
    public ScoreDto findById(Long id) {
        String sql = "SELECT * from score where id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, id);
    }

    @Override
    public ScoreDto findByGameId(final Long gameId) {
        String sql = "SELECT * from score where game_id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }
}
