package chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import chess.dto.BoardDto;

public class BoardDao {
	private static final BoardDao instance = new BoardDao();

	private BoardDao() {
	}

	public static BoardDao getInstance() {
		return instance;
	}

	public void saveBoard(BoardDto boardDTO) throws SQLException {
		Map<String, String> board = boardDTO.getBoard();

		String query = "INSERT INTO board VALUES (?, ?)";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(query)) {
			for (String position : board.keySet()) {
				pstmt.setString(1, position);
				pstmt.setString(2, board.get(position));
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}
	}

	public void deleteAll() throws SQLException {
		String query = "DELETE FROM board";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.executeUpdate();
		}
	}

	public BoardDto findBoard() throws SQLException {
		Map<String, String> board = new HashMap<>();
		String query = "SELECT * FROM board";
		try (Connection connection = DBConnection.getConnection();
			 PreparedStatement pstmt = connection.prepareStatement(query);
			 ResultSet rs = pstmt.executeQuery()) {

			checkEmpty(rs);

			do {
				board.put(rs.getString(1), rs.getString(2));
			} while (rs.next());

			return new BoardDto(board);
		}
	}

	private void checkEmpty(ResultSet rs) throws SQLException {
		if (!rs.next()) {
			throw new NoSuchElementException("Board 데이터베이스가 비었습니다.");
		}
	}
}

