package chess.dao;

import chess.domain.ChessGame;
import chess.domain.state.Turn;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public ChessGame createChessGame(ChessGame chessGame) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(chessGame);
        long id = insertActor.executeAndReturnKey(sqlParameterSource).longValue();

        return new ChessGame(id, chessGame.getTurn(), chessGame.getTitle(), chessGame.getPassword());
    }

    public Turn findChessGame(long id) {
        String sql = "select turn from chess_game where id = ?";

        return jdbcTemplate.queryForObject(sql, Turn.class, id);
    }

    public int changeChessGameTurn(long id, Turn turn) {
        String sql = "update chess_game set turn = ? where id = ?";

        return jdbcTemplate.update(sql, turn.name(), id);
    }
}
