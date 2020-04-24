package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDao {
	public int findTurnPlayerId(int roomId) throws SQLException, ClassNotFoundException {
		String query = "select turn from room where room_id = (?)";
		try (Connection con = ConnectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, roomId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
				throw new IllegalArgumentException("Turn이 잘못되었습니다.");
			}
		}
	}

	public int create(int player1Id, int player2Id) throws SQLException, ClassNotFoundException {
		String query = "insert into room(turn, player1_id, player2_id) value (?, ?, ?)";
		try (Connection con = ConnectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query,
			PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, player1Id);
			pstmt.setInt(2, player1Id);
			pstmt.setInt(3, player2Id);
			pstmt.executeUpdate();
			return getId(pstmt);
		}

	}

	private int getId(PreparedStatement pstmt) throws SQLException {
		try (ResultSet rs = pstmt.getGeneratedKeys()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
			throw new IllegalArgumentException();
		}
	}

	public void updateTurn(int roomId) throws SQLException, ClassNotFoundException {
		String query = "";
		if (findTurnPlayerId(roomId) == findFirstPlayerId(roomId)) {
			query = "update room set turn = player2_id where room_id = (?)";
		} else {
			query = "update room set turn = player1_id where room_id = (?)";
		}
		try (Connection con = ConnectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, roomId);
			pstmt.executeUpdate();
		}
	}

	private int findFirstPlayerId(int roomId) throws SQLException, ClassNotFoundException {
		String query = "select player1_id from room where room_id = (?)";
		try (Connection con = ConnectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, roomId);
			ResultSet resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			throw new IllegalAccessError();
		}
	}
}
