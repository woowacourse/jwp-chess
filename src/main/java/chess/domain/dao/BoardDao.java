package chess.domain.dao;

import chess.service.dto.PieceDto;
import chess.utils.exception.NoExecuteQuery;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BoardDao {

    private static final int EMPTY_RESULT = 0;

    private final JdbcTemplate jdbcTemplate;

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(int gameId, String position, String piece, String color) {
        final String sql = "insert into Board (game_id, position, piece, color) values(?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, gameId, position, piece, color);
        } catch (Exception e) {
            throw new NoExecuteQuery();
        }
    }

    public List<PieceDto> findByGameId(int gameId) {
        final String sql = "select * from board where game_id = ?";
        try {
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> makePieceDto(resultSet), gameId);
        } catch (Exception e) {
            throw new NoExecuteQuery();
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
            jdbcTemplate.update("alter table Board alter column id restart with 1");
        } catch (Exception e) {
            throw new NoExecuteQuery();
        }
    }

    private void runDelete(int gameId) {
        final String sql = "delete from Board where game_id = ?";
        try {
            jdbcTemplate.update(sql, gameId);
        } catch (Exception e) {
            throw new NoExecuteQuery();
        }
    }

    private boolean isSavedGameExist(int gameId) {
        return gameId == EMPTY_RESULT;
    }

    public void updateMovePiece(int gameId, String source, String target) {
        deletePiece(gameId, target);
        String sql = "update Board set position = ? where game_id = ? and position = ?";
        try {
            jdbcTemplate.update(sql, target, gameId, source);
        } catch (Exception e) {
            throw new NoExecuteQuery();
        }
    }

    private void deletePiece(int gameId, String target) {
        String sql = "delete from Board where game_id = ? and position = ?";
        try {
            jdbcTemplate.update(sql, gameId, target);
        } catch (EmptyResultDataAccessException e) {
            return;
        } catch (Exception e) {
            throw new NoExecuteQuery();
        }
    }
}
