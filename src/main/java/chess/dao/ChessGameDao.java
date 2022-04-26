package chess.dao;

import chess.domain.ChessGame;
import chess.domain.state.Turn;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ChessGameDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("chess_game")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<ChessGame> rowMapper() {
        return (rs, rowNum) -> {
            final long id = rs.getLong("id");
            final String turn = rs.getString("turn");
            final String title = rs.getString("title");
            final String password = rs.getString("password");
            return new ChessGame(id, turn, title, password);
        };
    }

    public ChessGame createChessGame(ChessGame chessGame) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(chessGame);
        long id = insertActor.executeAndReturnKey(sqlParameterSource).longValue();

        return new ChessGame(id, chessGame.getTurn(), chessGame.getTitle(), chessGame.getPassword());
    }

    public ChessGame findChessGame(long id) {
        String sql = "select * from chess_game where id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    public int changeChessGameTurn(long id, Turn turn) {
        String sql = "update chess_game set turn = ? where id = ?";

        return jdbcTemplate.update(sql, turn.name(), id);
    }
}
