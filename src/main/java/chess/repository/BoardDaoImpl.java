package chess.repository;

import chess.domain.Color;
import chess.domain.GameState;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDaoImpl implements BoardDao {

    private static final String TABLE_NAME = "board";
    private static final String KEY_NAME = "id";

    private final SimpleJdbcInsert insertActor;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BoardDaoImpl(DataSource dataSource,
        NamedParameterJdbcTemplate jdbcTemplate) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(int roomId, GameState gameState) {
        return insertActor.executeAndReturnKey(Map.of("room_id", roomId, "turn", gameState.getTurn()))
            .intValue();
    }

    @Override
    public Color getTurn(int boardId) {
        String sql = "select turn from board where id = :boardId";
        return jdbcTemplate.queryForObject(sql, Map.of("boardId", boardId),
            (resultSet, rowNum) -> Color.valueOf(resultSet.getString(1).toUpperCase()));
    }

    @Override
    public int getBoardIdByRoom(int roomId) {
        String sql = "select id from board where room_id = :roomId";
        try {
            return jdbcTemplate.queryForObject(sql, Map.of("roomId", roomId), Integer.class);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("체스판이 존재하지 않습니다.");
        }
    }

    @Override
    public void updateTurn(int boardId, GameState gameState) {
        String sql = "update board set turn = :turn where id = :boardId";
        jdbcTemplate.update(sql, Map.of("turn", gameState.getTurn(), "boardId", boardId));
    }

    @Override
    public void deleteByRoom(int roomId) {
        String sql = "delete from board where room_id = :roomId";
        jdbcTemplate.update(sql, Map.of("roomId", roomId));
    }
}
