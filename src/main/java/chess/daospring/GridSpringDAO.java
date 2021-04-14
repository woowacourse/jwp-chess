package chess.daospring;

import chess.domain.grid.Grid;
import chess.domain.grid.gridStrategy.NormalGridStrategy;
import chess.domain.piece.Color;
import chess.dto.GridDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class GridSpringDAO {
    private JdbcTemplate jdbcTemplate;

    public GridSpringDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createGrid(long roomId) {
        Grid grid = new Grid(new NormalGridStrategy());
        boolean isBlackTurn = grid.isMyTurn(Color.BLACK);
        boolean isFinished = grid.isFinished();

        String query = "INSERT INTO grid (isBlackTurn, isFinished, roomId) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, isBlackTurn);
            ps.setBoolean(2, isFinished);
            ps.setLong(3, roomId);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public GridDto findGridByGridId(long gridId) {
        String query = "SELECT * FROM grid WHERE gridId = ? LIMIT 1";
        return jdbcTemplate.queryForObject(
                query,
                getGridDtoRowMapper(),
                gridId);
    }

    private RowMapper<GridDto> getGridDtoRowMapper() {
        return (resultSet, rowNum) -> {
            return new GridDto(
                    resultSet.getLong("gridId"),
                    resultSet.getBoolean("isBlackTurn"),
                    resultSet.getBoolean("isFinished"),
                    resultSet.getLong("roomId"),
                    resultSet.getObject("createdAt", LocalDateTime.class),
                    resultSet.getBoolean("isStarted")
            );
        };
    }

    public GridDto findRecentGridByRoomId(long roomId) throws SQLException {
        String query = "SELECT * FROM grid WHERE roomId = ? ORDER BY createdAt DESC LIMIT 1";
        return jdbcTemplate.queryForObject(query, getGridDtoRowMapper(), roomId);
    }

    public void changeToStarting(long gridId) throws SQLException {
        String query = "UPDATE grid SET isStarted = true WHERE gridId = ?";
        jdbcTemplate.update(query, gridId);
    }

    public void changeTurn(long gridId, boolean isBlackTurn) throws SQLException {
        String query = "UPDATE grid SET isBlackTurn = ? WHERE gridId = ?";
        jdbcTemplate.update(query, isBlackTurn, gridId);
    }

    public void changeToFinished(long gridId) throws SQLException {
        String query = "UPDATE grid SET isFinished = true WHERE gridId = ?";
        jdbcTemplate.update(query, gridId);
    }
}
