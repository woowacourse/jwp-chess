package chess.dao;

import chess.dto.CreateGameRequest;
import chess.dto.GameInfoDto;
import chess.util.DaoUtil;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<GameInfoDto> eventRowMapper = (resultSet, rowNum) ->
            new GameInfoDto(resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("running")
            );

    public List<GameInfoDto> selectAll() {
        final String sql = "SELECT id, title, password, running FROM game";
        return DaoUtil.queryNoParameter(namedParameterJdbcTemplate, sql, eventRowMapper);
    }

    public GameInfoDto findById(int id) {
        final String sql = "SELECT id, title, password, running FROM game WHERE id = :id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);

        return DaoUtil.queryForObject(namedParameterJdbcTemplate, sql, paramSource, eventRowMapper);
    }

    public int saveAndGetGeneratedId(CreateGameRequest request) {
        final String sql = "INSERT INTO game(title, password) VALUES (:title, :password)";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("title", request.getTitle());
        paramSource.addValue("password", request.getPassword());

        return DaoUtil.queryForKeyHolder(namedParameterJdbcTemplate, sql, paramSource);
    }

    public void finishGame(int gameId) {
        final String sql = "UPDATE game SET running = false WHERE id = :game_id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("game_id", gameId);

        namedParameterJdbcTemplate.update(sql, paramSource);
    }

    public boolean checkById(int gameId) {
        final String sql = "SELECT COUNT(*) FROM game WHERE id = :game_id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource("game_id", gameId);

        Integer existingGameCount = namedParameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
        return existingGameCount == null || existingGameCount > 0;
    }

    public int countAll() {
        final String sql = "SELECT COUNT(*) FROM game";
        return DaoUtil.queryForInt(namedParameterJdbcTemplate, sql, new EmptySqlParameterSource());
    }

    public int countRunningGames() {
        final String sql = "SELECT COUNT(*) FROM game WHERE running = true";
        return DaoUtil.queryForInt(namedParameterJdbcTemplate, sql, new EmptySqlParameterSource());
    }

    public int delete(int id) {
        final String sql = "delete from game where id = :id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }
}
