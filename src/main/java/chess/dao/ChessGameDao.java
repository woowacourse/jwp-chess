package chess.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import chess.dto.ChessGameDto;
import chess.model.ChessGame;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChessGame> chessGameRowMapper = (resultSet, rowNum) -> new ChessGame(
        resultSet.getLong("id"),
        resultSet.getString("title"),
        resultSet.getString("password")
    );

    public void insert(ChessGameDto chessGameDto) {
        final String sql = "insert into game (title, password) values (?, ?)";
        jdbcTemplate.update(sql, chessGameDto.getTitle(), chessGameDto.getPassword());
    }

    public ChessGame find(Long id) {
        final String sql = "select * from game where id = ?";
        return jdbcTemplate.queryForObject(
            sql,
            (resultSet, rowNum) -> new ChessGame(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("password")
            ), id);
    }

    public int delete(Long id) {
        final String sql = "delete from game where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<ChessGame> findAll() {
        final String sql = "select * from game";
        return jdbcTemplate.query(sql, chessGameRowMapper);
    }
}
