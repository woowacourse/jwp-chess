package chess.repository.dao;

import chess.repository.entity.ChessGameEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ChessGameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(final ChessGameEntity chessGameEntity) {
        String insertSql = "insert into chess_game (game_room_id, is_on, team_value_of_turn)"
                + " values (:gameRoomId, :isOn, :teamValueOfTurn)";
        SqlParameterSource source = new BeanPropertySqlParameterSource(chessGameEntity);
        namedParameterJdbcTemplate.update(insertSql, source);
    }

    public ChessGameEntity load(final String gameRoomId) {
        String selectSql = "select game_room_id, is_on, team_value_of_turn from chess_game where game_room_id=:gameRoomId";
        SqlParameterSource source = new MapSqlParameterSource("gameRoomId", gameRoomId);
        return namedParameterJdbcTemplate.queryForObject(selectSql, source, getChessGameEntityRowMapper());
    }

    private RowMapper<ChessGameEntity> getChessGameEntityRowMapper() {
        return (rs, rn) -> new ChessGameEntity(
                rs.getString("game_room_id"),
                rs.getBoolean("is_on"),
                rs.getString("team_value_of_turn")
        );
    }

    public void update(final ChessGameEntity chessGameEntity) {
        String updateSql = "update chess_game set is_on=:isOn, team_value_of_turn=:teamValueOfTurn where game_room_id=:gameRoomId";
        SqlParameterSource source = new BeanPropertySqlParameterSource(chessGameEntity);
        namedParameterJdbcTemplate.update(updateSql, source);
    }
}
