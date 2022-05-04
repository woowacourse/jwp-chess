package chess.repository;

import chess.domain.Color;
import chess.dto.GameStateDto;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {

    private static final String TABLE_NAME = "board";
    private static final String KEY_NAME = "id";

    private final SimpleJdbcInsert insertActor;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BoardDao(DataSource dataSource,
                    NamedParameterJdbcTemplate jdbcTemplate) {
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(int roomId, GameStateDto gameStateDto) {
        return insertActor.executeAndReturnKey(
                        Map.of("room_id", roomId,
                                "turn", gameStateDto.getTurn(),
                                "end", gameStateDto.getEnd()))
                .intValue();
    }

    public Color getTurn(int boardId) {
        String sql = "select turn from board where id = :boardId";
        return jdbcTemplate.queryForObject(sql, Map.of("boardId", boardId),
                (resultSet, rowNum) -> Color.valueOf(resultSet.getString(1).toUpperCase()));
    }

    public boolean getEnd(int boardId) {
        String sql = "select end from board where id = :boardId";
        return jdbcTemplate.queryForObject(sql, Map.of("boardId", boardId), Boolean.class);
    }

    public Optional<Integer> findBoardIdByRoom(int roomId) {
        String sql = "select id from board where room_id = :roomId";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, Map.of("roomId", roomId), Integer.class));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void updateState(int boardId, GameStateDto gameStateDto) {
        String sql = "update board set turn = :turn, end = :end where id = :boardId";
        jdbcTemplate.update(sql,
                Map.of("turn", gameStateDto.getTurn(),
                        "boardId", boardId,
                        "end", gameStateDto.getEnd()));
    }

    public void deleteByRoom(int roomId) {
        String sql = "delete from board where room_id = :roomId";
        jdbcTemplate.update(sql, Map.of("roomId", roomId));
    }
}
