package chess.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import chess.dto.BoardDto;

@Repository
public class BoardJdbcDao implements BoardDao {

    private static final RowMapper<BoardDto> BOARD_DTO_ROW_MAPPER = (resultSet, rowNum) ->
        new BoardDto(resultSet.getString("symbol"),
            resultSet.getString("team"),
            resultSet.getString("position"));

    private final JdbcTemplate jdbcTemplate;

    public BoardJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BoardDto> findByGameId(int gameId) {
        final String sql = "select symbol, team, position from board where game_id = (?)";
        return jdbcTemplate.query(sql, BOARD_DTO_ROW_MAPPER, gameId);
    }

    @Override
    public void save(List<BoardDto> boardDtos, int gameId) {
        final String sql = "insert into board (symbol, team, position, game_id) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BoardDto boardDto = boardDtos.get(i);
                ps.setString(1, boardDto.getSymbol());
                ps.setString(2, boardDto.getTeam());
                ps.setString(3, boardDto.getPosition());
                ps.setInt(4, gameId);
            }

            @Override
            public int getBatchSize() {
                return boardDtos.size();
            }
        });
    }

    @Override
    public void update(BoardDto boardDto, int gameId) {
        final String sql = "update board set symbol = (?), team = (?) where position = (?) and game_id = (?)";
        jdbcTemplate.update(sql, boardDto.getSymbol(), boardDto.getTeam(), boardDto.getPosition(), gameId);
    }
}
