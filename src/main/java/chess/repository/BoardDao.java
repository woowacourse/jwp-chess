package chess.repository;

import chess.repository.entity.BoardEntity;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BoardDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(final List<BoardEntity> boardEntities) {
        String insertSql =
                "insert into board (game_room_id, position_column_value, position_row_value, piece_name, piece_team_value)"
                        + " values (:gameRoomId, :positionColumnValue, :positionRowValue, :pieceName, :pieceTeamValue)";
        for (BoardEntity boardEntity : boardEntities) {
            SqlParameterSource source = new BeanPropertySqlParameterSource(boardEntity);
            namedParameterJdbcTemplate.update(insertSql, source);
        }
    }

    public List<BoardEntity> load(final String gameRoomId) {
        String selectSql = "select * from board where game_room_id=:gameRoomId";
        SqlParameterSource source = new MapSqlParameterSource("gameRoomId", gameRoomId);
        List<BoardEntity> boardEntities =
                namedParameterJdbcTemplate.query(selectSql, source, getBoardEntityRowMapper());
        validateBoardExist(boardEntities);
        return boardEntities;
    }

    private RowMapper<BoardEntity> getBoardEntityRowMapper() {
        return (rs, rn) -> new BoardEntity(
                rs.getString("game_room_id"),
                rs.getString("position_column_value"),
                rs.getInt("position_row_value"),
                rs.getString("piece_name"),
                rs.getString("piece_team_value")
        );
    }

    private void validateBoardExist(final List<BoardEntity> boardEntities) {
        if (boardEntities.size() == 0) {
            throw new IllegalArgumentException("[ERROR] Board 가 존재하지 않습니다.");
        }
    }

    public void updateBoard(final BoardEntity boardEntity) {
        String updateSql = "update board set piece_name=:pieceName, piece_team_value=:pieceTeamValue"
                + " where game_room_id=:gameRoomId"
                + " and position_column_value=:positionColumnValue"
                + " and position_row_value=:positionRowValue";
        SqlParameterSource source = new BeanPropertySqlParameterSource(boardEntity);
        namedParameterJdbcTemplate.update(updateSql, source);
    }
}
