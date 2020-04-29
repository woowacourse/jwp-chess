package wooteco.chess.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import wooteco.chess.db.ConnectionLoader;
import wooteco.chess.dto.RoomDto;

@Component
public class RoomDao {
	private final ConnectionLoader connectionLoader;

	public RoomDao(ConnectionLoader connectionLoader) {
		this.connectionLoader = connectionLoader;
	}

	public RoomDto findById(int roomId) throws SQLException {
		String query = "select * from room where room_id = (?)";
		try (Connection con = connectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, roomId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					RoomDto roomDto = new RoomDto();
					roomDto.setId(rs.getLong(1));
					roomDto.setTurnId(rs.getInt(2));
					roomDto.setPlayer1Id(rs.getInt(3));
					roomDto.setPlayer2Id(rs.getInt(4));
					return roomDto;
				}
				throw new IllegalArgumentException("존재하지 않는 방입니다.");
			}
		}
	}

	public int create(int player1Id, int player2Id) throws SQLException {
		String query = "insert into room(turn, player1_id, player2_id) value (?, ?, ?)";
		try (Connection con = connectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query,
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

	public void updateTurn(int roomId, int turn) throws SQLException {
		String query = "update room set turn = (?) where room_id = (?)";
		try (Connection con = connectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, turn);
			pstmt.setInt(2, roomId);
			pstmt.executeUpdate();
		}
	}
}
