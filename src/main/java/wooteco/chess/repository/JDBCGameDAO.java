package wooteco.chess.repository;

import java.util.Objects;
import java.util.Optional;

import wooteco.chess.domain.board.BoardParser;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.game.GameStateFactory;
import wooteco.chess.utils.jdbc.JDBCTemplate;

public class JDBCGameDAO implements GameDAO {
	private final JDBCTemplate jdbcTemplate;

	public JDBCGameDAO(JDBCTemplate jdbcTemplate) {
		this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate);
	}

	@Override
	public Optional<Game> findById(int gameId) {
		String query = "SELECT * FROM game WHERE id = ?";
		return jdbcTemplate.executeQuery(query, rs ->
			GameStateFactory.of(
				rs.getString("state"),
				rs.getString("turn"),
				rs.getString("board")
			), pstmt -> pstmt.setInt(1, gameId));
	}

	@Override
	public void update(Game game) {
		String query = "UPDATE game SET state=?, turn=?, board=? WHERE id=?";
		jdbcTemplate.executeUpdate(query, preparedStatement -> {
			preparedStatement.setString(1, game.getStateType());
			preparedStatement.setString(2, game.getTurn().name());
			preparedStatement.setString(3, BoardParser.parseString(game.getBoard()));
			preparedStatement.setInt(4, game.getId());
		});
	}
}