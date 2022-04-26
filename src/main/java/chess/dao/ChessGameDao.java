package chess.dao;

import chess.entity.ChessGameEntity;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChessGameDao(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Number save(final ChessGameEntity chessGameEntity) {
        String insertSql = "insert into chess_game (name, is_on, team_value_of_turn)"
                + " values (:name, :isOn, :teamValueOfTurn)";
        SqlParameterSource source = new BeanPropertySqlParameterSource(chessGameEntity);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(insertSql, source, keyHolder);
        return keyHolder.getKey();
    }

    public void delete(final long id) {
        String deleteSql = "delete from chess_game where id=:id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(deleteSql, source);
    }

    public ChessGameEntity load(final long id) {
        String selectSql = "select * from chess_game where id=:id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                selectSql,
                source,
                getChessGameEntityRowMapper());
    }

    public List<ChessGameEntity> loadAll() {
        String loadAllSql = "select * from chess_game";
        return namedParameterJdbcTemplate.query(loadAllSql, getChessGameEntityRowMapper());
    }

    private RowMapper<ChessGameEntity> getChessGameEntityRowMapper() {
        return (rs, rn) -> new ChessGameEntity(
                rs.getLong(("id")),
                rs.getString("name"),
                rs.getBoolean("is_on"),
                rs.getString("team_value_of_turn")
        );
    }

    public void updateIsOnAndTurn(final ChessGameEntity chessGameEntity) {
        String updateSql = "update chess_game set is_on=:isOn, team_value_of_turn=:teamValueOfTurn where name=:name";
        SqlParameterSource source = new BeanPropertySqlParameterSource(chessGameEntity);
        namedParameterJdbcTemplate.update(updateSql, source);
    }
}
