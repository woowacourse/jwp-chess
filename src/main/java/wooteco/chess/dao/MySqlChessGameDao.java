package wooteco.chess.dao;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import wooteco.chess.database.CustomJdbcTemplate;
import wooteco.chess.entity.ChessGameEntity;

@Repository
public class MySqlChessGameDao implements ChessGameDao {

	public static final long EMPTY_CHESS_GAME = 0L;
	private static final String CHESS_GAME_TABLE = "chess_games";

	private final CustomJdbcTemplate customJdbcTemplate;

	public MySqlChessGameDao(final CustomJdbcTemplate customJdbcTemplate) {
		Objects.requireNonNull(customJdbcTemplate, "JdbcTemplate이 null입니다.");
		this.customJdbcTemplate = customJdbcTemplate;
	}

	@Override
	public long add(final ChessGameEntity entity) {
		Objects.requireNonNull(entity, "엔티티가 null입니다.");
		final String query = "INSERT INTO " + CHESS_GAME_TABLE + " (created_time) VALUES (?)";

		return customJdbcTemplate.executeUpdate(query, preparedStatement -> {
			preparedStatement.setTimestamp(1, Timestamp.valueOf(entity.getCreatedTime()));
		});
	}

	@Override
	public long findMaxGameId() {
		final String query = "SELECT MAX(game_id) AS max_id FROM " + CHESS_GAME_TABLE;

		return customJdbcTemplate.executeQuery(query, resultSet -> {
			if (resultSet.next()) {
				return resultSet.getLong("max_id");
			}
			return EMPTY_CHESS_GAME;
		});
	}

	@Override
	public boolean isEmpty() {
		final String query = "SELECT * FROM " + CHESS_GAME_TABLE;

		return !customJdbcTemplate.executeQuery(query, ResultSet::next);
	}

	@Override
	public void deleteAll() {
		final String query = "DELETE FROM " + CHESS_GAME_TABLE;

		customJdbcTemplate.executeUpdate(query);
	}

}
