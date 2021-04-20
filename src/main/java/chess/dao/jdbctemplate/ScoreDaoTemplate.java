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
                    resultSet.getDouble("white_score"),
                    resultSet.getDouble("black_score")
            );

    @Override
    public Long saveScore(final GameStatus gameStatus, final Long gameId) {
        String sql = "INSERT INTO score(game_id, white_score, black_score) VALUES (?, ?, ?)";
        return (long) jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, gameId);
            ps.setDouble(2, gameStatus.whiteScore());
            ps.setDouble(3, gameStatus.blackScore());
            return ps;
        }, new GeneratedKeyHolder());
    }

    @Override
    public Long updateScore(final GameStatus gameStatus, final Long gameId) {
        String sql = "UPDATE score SET white_score=?, black_score=? WHERE game_id=?";
        return (long) jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setDouble(1, gameStatus.whiteScore());
            ps.setDouble(2, gameStatus.blackScore());
            ps.setLong(3, gameId);
            return ps;
        }, new GeneratedKeyHolder());
    }

    @Override
    public ScoreDto findScoreByGameId(final Long gameId) {
        String sql = "SELECT * from score where game_id = ?";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, gameId);
    }
}
