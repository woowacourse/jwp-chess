package chess.dao.spring;

import chess.dao.PlayLogDao;
import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SpringPlayLogDao implements PlayLogDao {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JdbcTemplate jdbcTemplate;

    public SpringPlayLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(BoardDto boardDto, GameStatusDto gameStatusDto, String roomId) {
        String query = "INSERT INTO play_log (board, game_status, room_id) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(query, OBJECT_MAPPER.writeValueAsString(boardDto),
                OBJECT_MAPPER.writeValueAsString(gameStatusDto), roomId);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("직렬화에 실패했습니다.");
        }
    }

    public BoardDto latestBoard(String roomId) {
        String query = "SELECT board FROM play_log WHERE room_id = (?) ORDER BY last_played_time DESC, id DESC LIMIT 1";

        return jdbcTemplate.queryForObject(
            query,
            (resultSet, rowNum) -> {
                String boardJson = resultSet.getString(1);
                try {
                    return OBJECT_MAPPER.readValue(boardJson, BoardDto.class);
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException("역직렬화에 실패했습니다.");
                }
            },
            roomId);
    }

    public GameStatusDto latestGameStatus(String roomId) {
        String query = "SELECT game_status FROM play_log WHERE room_id = (?) ORDER BY last_played_time DESC, id DESC LIMIT 1";

        return jdbcTemplate.queryForObject(
            query,
            (resultSet, rowNum) -> {
                String statusJson = resultSet.getString(1);
                try {
                    return OBJECT_MAPPER.readValue(statusJson, GameStatusDto.class);
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException("역직렬화에 실패했습니다.");
                }
            },
            roomId);
    }
}
