package chess.domain.dao;

import chess.domain.dto.PieceDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BoardJdbcTemplateDao {

    private static final int EMPTY_RESULT = 0;

    private final JdbcTemplate jdbcTemplate;

    public BoardJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(int gameId, String position, String piece, String color) {
        final String sql = "insert into Board (game_id, position, piece, color) values(?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, gameId, position, piece, color);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    public List<PieceDto> findByGameId(int gameId) {
        final String sql = "select * from board where game_id = ?";
        try {
            return jdbcTemplate.query(sql, new RowMapper<PieceDto>() {
                @Override
                public PieceDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                    return makePieceDto(resultSet);
                }
            }, gameId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    private PieceDto makePieceDto(ResultSet resultSet) throws SQLException {
        return new PieceDto(
                resultSet.getInt("game_id"),
                resultSet.getString("position"),
                resultSet.getString("piece"),
                resultSet.getString("color")
        );
    }

    public void delete(int gameId) {
        if (isSavedGameExist(gameId)) {
            return;
        }
        runDelete(gameId);
    }

    public void deleteAll() {
        final String sql = "delete from Board";
        try {
            jdbcTemplate.update(sql);
            jdbcTemplate.update("alter table game auto_increment = 1");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    private void runDelete(int gameId) {
        final String sql = "delete from Board where game_id = ?";
        try {
            jdbcTemplate.update(sql, gameId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("요청이 정상적으로 실행되지 않았습니다.");
        }
    }

    private boolean isSavedGameExist(int gameId) {
        return gameId == EMPTY_RESULT;
    }
}
