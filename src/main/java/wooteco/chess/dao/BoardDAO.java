package wooteco.chess.dao;

import static wooteco.chess.dao.DBConnector.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.piece.Symbol;
import wooteco.chess.domain.position.Column;
import wooteco.chess.domain.position.Position;

@Component
public class BoardDAO {
	public void addPieces(String gameId, List<Piece> pieces) {
		String query = "INSERT INTO board (game_id, position, symbol) VALUES (?, ?, ?)";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			setStatementByPieceIn(gameId, pieces, pstmt);
			pstmt.executeBatch();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private void setStatementByPieceIn(String gameId, List<Piece> pieces, PreparedStatement pstmt) throws SQLException {
		for (Piece piece : pieces) {
			pstmt.setString(1, gameId);
			pstmt.setString(2, piece.getPosition().getName());
			pstmt.setString(3, piece.getSymbol());
			pstmt.addBatch();
			pstmt.clearParameters();
		}
	}

	public void addPiece(String gameId, Piece piece) {
		String query = "INSERT INTO board (game_id, position, symbol) VALUES (?, ?, ?)";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, gameId);
			pstmt.setString(2, piece.getPosition().getName());
			pstmt.setString(3, piece.getSymbol());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void update(String gameId, Position from, Position to) {
		String query = "UPDATE board SET symbol = ? WHERE game_id = ? AND position = ?";
		Piece source = findPieceBy(gameId, from);
		Piece target = findPieceBy(gameId, to);
		try (Connection con = getConnection();
			 PreparedStatement firstPstmt = con.prepareStatement(query);
			 PreparedStatement secondPstmt = con.prepareStatement(query)) {
			firstPstmt.setString(1, source.getSymbol());
			firstPstmt.setString(2, gameId);
			firstPstmt.setString(3, target.getPosition().getName());
			firstPstmt.executeUpdate();

			secondPstmt.setString(1, Symbol.EMPTY.getBlackSymbol());
			secondPstmt.setString(2, gameId);
			secondPstmt.setString(3, source.getPosition().getName());
			secondPstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public boolean hasNotGameIn(String gameId) {
		String query = "SELECT game_id FROM board WHERE game_id = ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, gameId);
			ResultSet rs = pstmt.executeQuery();
			return !rs.next();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public List<Piece> findAllPieces(String gameId) {
		List<Piece> result = new ArrayList<>();
		String query = "SELECT * FROM board WHERE game_id = ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, gameId);
			return getPieces(result, pstmt);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private List<Piece> getPieces(List<Piece> result, PreparedStatement pstmt) throws SQLException {
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			String symbol = rs.getString("symbol");
			Position position = Position.of(rs.getString("position"));
			result.add(PieceFactory.of(symbol).create(position));
		}

		return result;
	}

	public List<Piece> findGroupBy(String gameId, Column column) {
		List<Piece> result = new ArrayList<>();
		String query = "SELECT * FROM board WHERE game_id = ? AND position LIKE ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, gameId);
			pstmt.setString(2, column.getName() + "%");
			return getPieces(result, pstmt);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Piece findPieceBy(String gameId, Position position) {
		String query = "SELECT * FROM board WHERE game_id = ? AND position = ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, gameId);
			pstmt.setString(2, position.getName());
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new IllegalArgumentException("기물이 존재하지 않습니다. position : " + position.getName());
			}
			return PieceFactory.of(rs.getString("symbol")).create(position);
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void truncate() {
		String query = "TRUNCATE table board";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}