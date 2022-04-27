package chess.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import chess.dto.ChessGameDto;
import chess.entity.ChessGameEntity;

@Repository
public class ChessGameDaoImpl implements ChessGameDao {

    private JdbcTemplate jdbcTemplate;

    public ChessGameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChessGameEntity> chessGameEntityRowMapper = (resultSet, rowNum) -> new ChessGameEntity(
        resultSet.getLong("id"),
        resultSet.getString("title"),
        resultSet.getString("password")
    );

    @Override
    public void insert(ChessGameDto chessGameDto) {
        final String sql = "insert into game (title, password) values (?, ?)";
        jdbcTemplate.update(sql, chessGameDto.getTitle(), chessGameDto.getPassword());
    }

    @Override
    public ChessGameEntity find(Long id) {
        final String sql = "select * from game where id = ?";
        return jdbcTemplate.queryForObject(
            sql,
            (resultSet, rowNum) -> new ChessGameEntity(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("password")
            ), id);
    }

    @Override
    public int delete(Long id) {
        final String sql = "delete from game where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<ChessGameEntity> findAll() {
        final String sql = "select * from game";
        return jdbcTemplate.query(sql, chessGameEntityRowMapper);
    }
}
