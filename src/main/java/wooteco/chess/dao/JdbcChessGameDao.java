package wooteco.chess.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.state.State;
import wooteco.chess.domain.game.state.StateFactory;
import wooteco.chess.dto.BoardDto;

@Component("JdbcChessGameDao")
public class JdbcChessGameDao implements ChessGameDao {
	private final JdbcTemplate jdbcTemplate;

	public JdbcChessGameDao(@Qualifier("CustomJdbcTemplate") JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int create() throws SQLException {
		PreparedStatementSetter setter = preparedStatement -> {
			preparedStatement.setString(1, "READY");
		};

		RowMapper<Integer> mapper = resultSet -> {
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			return 0;
		};

		return jdbcTemplate.executeUpdate("INSERT INTO chess_game(state) VALUES (?)", setter, mapper);
	}

	@Override
	public List<Integer> findAll() throws SQLException {
		RowMapper<List<Integer>> mapper = resultSet -> {
			List<Integer> ids = new ArrayList<>();
			while (resultSet.next()) {
				ids.add(resultSet.getInt(1));
			}
			return ids;
		};

		return jdbcTemplate.executeQuery("SELECT id FROM chess_game", mapper);
	}

	@Override
	public Optional<ChessGame> findById(int id) throws SQLException {
		PreparedStatementSetter setter = preparedStatement -> {
			preparedStatement.setInt(1, id);
		};

		RowMapper<Optional<ChessGame>> mapper = resultSet -> {
			if (resultSet.next()) {
				State state = StateFactory.valueOf(resultSet.getString(2))
						.create(resultSet.getString(3), resultSet.getString(4));
				return Optional.of(new ChessGame(state));
			}
			return Optional.empty();
		};

		return jdbcTemplate.executeQuery("SELECT * FROM chess_game WHERE id = ?", setter, mapper);
	}

	@Override
	public void updateById(int id, ChessGame chessGame) throws SQLException {
		PreparedStatementSetter setter = preparedStatement -> {
			preparedStatement.setString(1, chessGame.getState().toString());
			preparedStatement.setString(2, String.join("", new BoardDto(chessGame.board()).getBoard()));
			preparedStatement.setString(3, String.valueOf(chessGame.turn()));
			preparedStatement.setInt(4, id);
		};

		jdbcTemplate.executeUpdate("UPDATE chess_game SET state = ?, board = ?, turn = ? WHERE id = ?", setter);
	}

	@Override
	public void deleteById(int id) throws SQLException {
		PreparedStatementSetter setter = preparedStatement -> {
			preparedStatement.setInt(1, id);
		};

		jdbcTemplate.executeUpdate("DELETE FROM chess_game WHERE id = ?", setter);
	}
}
