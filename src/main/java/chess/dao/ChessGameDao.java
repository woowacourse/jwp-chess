package chess.dao;

import chess.domain.state.Turn;
import java.sql.PreparedStatement;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ChessGameDao {

	private final JdbcTemplate jdbcTemplate;

	public ChessGameDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public long createChessGame(Turn turn) {
		String sql = "insert into chess_game (turn) values (?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
					PreparedStatement statement = con.prepareStatement(sql, new String[]{"id"});
					statement.setString(1, turn.name());
					return statement;
				},
				keyHolder);
		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	public Turn findChessGame(long id) {
		String sql = "select turn from chess_game where id = ?";

		return jdbcTemplate.queryForObject(sql, Turn.class, id);
	}
}
