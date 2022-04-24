package chess.dao;

import chess.dto.BoardElementDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChessDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<BoardElementDto> boardDtoRowMapper = (resultSet, rowNum) -> {
        BoardElementDto boardDto = new BoardElementDto(
                resultSet.getInt("game_id"),
                resultSet.getString("piece_name"),
                resultSet.getString("piece_color"),
                resultSet.getString("position")
        );
        return boardDto;
    };

    public ChessDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("piece")
                .usingGeneratedKeyColumns("piece_id");
    }

    public static final int GAME_ID = 0;

    public void updateTurn(final String color, int gameId) {
        final var sql = "UPDATE game SET current_turn=? WHERE game_id=?";
        jdbcTemplate.update(sql, color, gameId);
    }

    public void deleteAllPiece(int gameId) {
        final var sql = "DELETE FROM piece WHERE game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void savePiece(List<BoardElementDto> boardDtos) {
        for (BoardElementDto dto : boardDtos) {
            SqlParameterSource params = new BeanPropertySqlParameterSource(dto);
            simpleJdbcInsert.execute(params);
        }
    }

    public List<BoardElementDto> findBoard(int gameId) {
        final var sql = "SELECT * FROM piece WHERE game_id=?";
        return jdbcTemplate.query(sql, boardDtoRowMapper, gameId);
    }

    public String findCurrentColor(int gameId) {
        final var sql = "SELECT current_turn FROM game WHERE game_id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

//    public void updateBoard(final String from, final String to, final String color) {
//        deletePiece(to);
//        updatePiece(from, to, GAME_ID);
//        updateTurn(color, GAME_ID);
//    }

    public void deletePiece(final String to) {
        final var sql = "DELETE FROM piece WHERE position = ?";
        jdbcTemplate.update(sql, to);
    }

    public void updatePiece(final String from, final String to, final int gameId) {
        final var sql = "UPDATE piece SET position = ? WHERE position = ? AND game_id = ?";
        jdbcTemplate.update(sql, to, from, gameId);
    }

    public void deleteGame(int gameId) {
        String sql = "delete from game where game_id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    public void initGame(int gameId) {
        String sql = "INSERT INTO game (game_id) VALUES(?)";
        jdbcTemplate.update(sql, gameId);
    }
}
