package wooteco.chess.domain.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import wooteco.chess.domain.BoardConverter;
import wooteco.chess.domain.ChessGame;
import wooteco.chess.domain.FinishFlag;

public class RoomDao extends MySqlDao {
	public List<String> findAll() throws SQLException {
		String query = "SELECT * FROM room";
		PreparedStatement pstmt = connect().prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();

		List<String> roomNames = new ArrayList<>();
		while (rs.next()) {
			roomNames.add(rs.getString("room_name"));
		}
		return roomNames;
	}

	public Optional<String> findByRoomName(String roomName, String columnLabel) throws SQLException {
		String query = "SELECT * FROM room WHERE room_name = ?";
		PreparedStatement pstmt = connect().prepareStatement(query);
		pstmt.setString(1, roomName);
		ResultSet rs = pstmt.executeQuery();

		if (!rs.next()) {
			return Optional.empty();
		}
		return Optional.of(rs.getString(columnLabel));
	}

	public int addRoom(String roomName, ChessGame chessGame) throws SQLException {
		String query = "INSERT INTO room(room_name, board, turn, finish_flag) VALUES (?, ?, ?, ?)";
		PreparedStatement pstmt = connect().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pstmt.setString(1, roomName);
		pstmt.setString(2, BoardConverter.convertToString(chessGame.getBoard()));
		pstmt.setString(3, chessGame.getTurn().name());
		pstmt.setString(4, FinishFlag.of(chessGame.isEnd()).getSymbol());
		pstmt.executeUpdate();

		ResultSet rs = pstmt.getGeneratedKeys();
		if (!rs.next()) {
			throw new IllegalArgumentException("DB에 방을 추가할 수 없습니다.");
		}
		return rs.getInt("GENERATED_KEY");
	}

	public void deleteRoom(String roomName) throws SQLException {
		String query = "DELETE FROM room WHERE room_name = ?";
		PreparedStatement pstmt = connect().prepareStatement(query);
		pstmt.setString(1, roomName);
		pstmt.executeUpdate();
	}

	public void updateRoom(String roomName, ChessGame chessGame) throws SQLException {
		String query = "UPDATE room SET board = ?, turn = ?, finish_flag = ? WHERE room_name = ?";
		PreparedStatement pstmt = connect().prepareStatement(query);
		pstmt.setString(1, BoardConverter.convertToString(chessGame.getBoard()));
		pstmt.setString(2, chessGame.getTurn().name());
		pstmt.setString(3, FinishFlag.of(chessGame.isEnd()).getSymbol());
		pstmt.setString(4, roomName);
		pstmt.executeUpdate();
	}
}
