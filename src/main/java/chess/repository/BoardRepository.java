package chess.repository;

import chess.dao.BoardDao;
import chess.entity.BoardEntity;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


@Repository
public class BoardRepository implements BoardDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public BoardRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
            .withTableName("board")
            .usingGeneratedKeyColumns("id");

    }

    @Override
    public List<BoardEntity> getBoard() {
        String sql = "select * from board";
        return jdbcTemplate.query(sql, rowMapper());
    }

    private RowMapper<BoardEntity> rowMapper() {
        return (rs, rowNum) -> {
            final Long id = rs.getLong("id");
            final Long roomId = rs.getLong("room_id");
            final String position = rs.getString("position");
            final String piece = rs.getString("piece");
            return new BoardEntity(id, roomId, position, piece);
        };
    }

    @Override
    public void updatePosition(final BoardEntity board) {
        final String sql = "update board set piece = ? where position =?";
        jdbcTemplate.update(sql, board.getPiece(), board.getPosition());
    }

    @Override
    public void updateBatchPositions(final List<BoardEntity> board) {
        final String sql = "update board set piece = ? where position =?";
        jdbcTemplate.batchUpdate(sql,
            board,
            board.size(),
            (PreparedStatement ps, BoardEntity boardEntity) -> {
                ps.setString(1, boardEntity.getPiece());
                ps.setString(2, boardEntity.getPosition());
            });
    }

    @Override
    public BoardEntity insert(final BoardEntity board) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(board);
        Long id = insertActor.executeAndReturnKey(parameters).longValue();
        return new BoardEntity(id, board.getRoomId(), board.getPosition(), board.getPiece());
    }

}
