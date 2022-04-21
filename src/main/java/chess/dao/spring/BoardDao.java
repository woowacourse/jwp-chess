package chess.dao.spring;

import chess.dao.entity.BoardEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class BoardDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BoardDao(final JdbcTemplate jdbcTemplate,
                    final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(final List<BoardEntity> boardEntities) {
        String insertSql = "insert into board (name, position_column_value, position_row_value, piece_name, piece_team_value) values (?, ?, ?, ?, ?)";
        for (BoardEntity boardEntity : boardEntities) {
            SqlParameterSource source = new BeanPropertySqlParameterSource(boardEntity);
            namedParameterJdbcTemplate.update(insertSql, source);
        }
    }

    public void delete(final String name) {
        String deleteSql = "delete from board where name=?";
        SqlParameterSource source = new MapSqlParameterSource("name", name);
        namedParameterJdbcTemplate.update(deleteSql, source);
    }

    public List<BoardEntity> load(final String name) {
        String selectSql = "select * from board where name=?";
        SqlParameterSource source = new MapSqlParameterSource("name", name);
        List<BoardEntity> boardEntities = jdbcTemplate.query(selectSql, (rs, rn) ->
                new BoardEntity(
                        rs.getString("name"),
                        rs.getString("position_column_value"),
                        rs.getInt("position_row_value"),
                        rs.getString("piece_name"),
                        rs.getString("piece_team_value")
                ), name);
        validateBoardExist(boardEntities);
        return boardEntities;
    }

    private void validateBoardExist(final List<BoardEntity> boardEntities) {
        if (boardEntities.size() == 0) {
            throw new IllegalArgumentException("[ERROR] Board 가 존재하지 않습니다.");
        }
    }
}
