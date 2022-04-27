package chess.dao;

import chess.domain.ChessGame;
import chess.domain.state.Turn;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
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

        try {
            long id = insertActor.executeAndReturnKey(sqlParameterSource).longValue();
            return new ChessGame(id, chessGame.getTurn(), chessGame.getTitle(), chessGame.getPassword());
        } catch (DuplicateKeyException duplicateKeyException){
            throw new IllegalArgumentException("중복된 체스 게임 제목이 이미 존재합니다.");
        }
    }

    public List<ChessGame> findAllChessGame() {
        String sql = "select * from chess_game";

        return jdbcTemplate.query(sql, rowMapper());
    }

    public ChessGame findChessGame(long id) {
        String sql = "select * from chess_game where id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    public int changeChessGameTurn(long id, Turn turn) {
        String sql = "update chess_game set turn = ? where id = ?";

        return jdbcTemplate.update(sql, turn.name(), id);
    }

    public int deleteChessGame(ChessGame chessGame) {
        String sql = "delete from chess_game where title = ?";

        return jdbcTemplate.update(sql, chessGame.getTitle());
    }
}
