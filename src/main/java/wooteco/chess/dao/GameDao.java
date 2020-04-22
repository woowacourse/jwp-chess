package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import wooteco.chess.dto.GameDto;

public class GameDao {
	private static final GameDao INSTANCE = new GameDao();
	private static final String TABLE_NAME = "GAME";

	private GameDao() {
	}

	public static GameDao getInstance() {
		return INSTANCE;
	}

	public GameDto save(GameDto gameDto) throws SQLException {
		String query = String.format("INSERT INTO %s (TURN) VALUES (?)", TABLE_NAME);
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
		) {
			pstmt.setString(1, gameDto.getTurn());
			pstmt.executeUpdate();

			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (!generatedKeys.next()) {
				throw new SQLException("저장 실패");
			}
			return new GameDto(generatedKeys.getLong(1), gameDto.getTurn());
		}
	}

	public void update(GameDto gameDto) throws SQLException {
		String query = String.format("UPDATE %s SET turn = ? WHERE id = ?", TABLE_NAME);
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setString(1, gameDto.getTurn());
			pstmt.setLong(2, gameDto.getId());
			pstmt.executeUpdate();
		}
	}

	public Optional<GameDto> findById(Long id) throws SQLException {
		String query = String.format("SELECT * FROM %s WHERE id = ?", TABLE_NAME);
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				return Optional.empty();
			}
			return Optional.of(new GameDto(rs.getLong("id"), rs.getString("turn")));
		}
	}

	public void deleteAll() throws SQLException {
		String query = String.format("DELETE FROM %s", TABLE_NAME);
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.executeUpdate();
		}
	}
}
