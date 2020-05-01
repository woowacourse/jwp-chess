package wooteco.chess.dao;

import static wooteco.chess.dao.DBConnector.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import wooteco.chess.domain.piece.Turn;

@Component
public class TurnInfoDAO {
	public void initialize(String gameId, Turn turn) {
		String query = "INSERT INTO turn_info (game_id, current_team) VALUES (?, ?)";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, gameId);
			pstmt.setString(2, turn.name());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Turn findCurrent(String gameId) {
		String query = "SELECT current_team FROM turn_info WHERE game_id = ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, gameId);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new IllegalArgumentException("현재 차례의 팀이 존재하지 않습니다. game id : " + gameId);
			}
			return Turn.valueOf(rs.getString("current_team"));
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void updateNext(String gameId) {
		String query = "UPDATE turn_info SET current_team = ? WHERE game_id = ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, findCurrent(gameId).next().name());
			pstmt.setString(2, gameId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void truncate() {
		String query = "TRUNCATE table turn_info";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
