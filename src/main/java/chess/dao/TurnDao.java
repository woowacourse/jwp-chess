package chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import chess.dto.TurnDto;

public class TurnDao {
	private static final TurnDao instance = new TurnDao();

	private TurnDao() {
	}

	public static TurnDao getInstance() {
		return instance;
	}

	public void saveTurn(TurnDto turnDTO) throws SQLException {
		String query = "INSERT INTO turn VALUES (?)";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, turnDTO.getCurrentTeam());
			pstmt.executeUpdate();
		}
	}

	public void deleteAll() throws SQLException {
		String query = "DELETE FROM turn";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.executeUpdate();
		}
	}

	public TurnDto findTurn() throws SQLException {
		String query = "SELECT * FROM turn";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(query);
			 ResultSet rs = pstmt.executeQuery()) {
			checkEmpty(rs);
			return TurnDto.from(rs.getString("teamName"));
		}
	}

	public void updateTurn(TurnDto turnDTO) throws SQLException {
		String query = "UPDATE turn SET teamName = ?";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, turnDTO.getCurrentTeam());
			pstmt.executeUpdate();
		}
	}

	private void checkEmpty(ResultSet rs) throws SQLException {
		if (!rs.next()) {
			throw new NoSuchElementException("Turn 데이터베이스가 비었습니다!");
		}
	}
}
