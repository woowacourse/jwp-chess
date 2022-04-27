package chess.util;

import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public final class DaoUtil {

    private DaoUtil() {
    }

    public static <T> T queryForObject(NamedParameterJdbcTemplate jdbcTemplate, String sql,
                                       SqlParameterSource paramSource, RowMapper<T> rowMapper) {
        try {
            return jdbcTemplate.queryForObject(sql, paramSource, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public static <T> List<T> queryNoParameter(NamedParameterJdbcTemplate jdbcTemplate, String sql,
                                               RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, new EmptySqlParameterSource(), rowMapper);
    }

    public static int queryForInt(NamedParameterJdbcTemplate jdbcTemplate, String sql, SqlParameterSource paramSource) {
        Integer number = jdbcTemplate.queryForObject(sql, paramSource, Integer.class);
        if (number == null) {
            return 0;
        }
        return number;
    }

    public static int queryForKeyHolder(NamedParameterJdbcTemplate jdbcTemplate, String sql,
                                        SqlParameterSource paramSource) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, paramSource, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
}
