package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.dto.PieceDto;
import wooteco.chess.util.PieceConverter;

public class PieceDao {
	private static final PieceDao PIECE_DAO = new PieceDao();
	private static final String TABLE_NAME = "PIECE";

	private PieceDao() {
	}

	public static PieceDao getInstance() {
		return PIECE_DAO;
	}

	public PieceDto save(PieceDto pieceDto) throws SQLException {
		String query = String.format("INSERT INTO %s (SYMBOL,GAME_ID,POSITION,TEAM) VALUES(?,?,?,?)", TABLE_NAME);
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
		) {
			pstmt.setString(1, pieceDto.getSymbol());
			pstmt.setLong(2, pieceDto.getGameId());
			pstmt.setString(3, pieceDto.getPosition());
			pstmt.setString(4, pieceDto.getTeam());
			pstmt.executeUpdate();

			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (!generatedKeys.next()) {
				throw new SQLException("저장 실패");
			}
			Long pieceId = generatedKeys.getLong(1);
			return new PieceDto(pieceId, pieceDto.getGameId(), pieceDto.getSymbol(), pieceDto.getTeam(),
				pieceDto.getPosition());
		}
	}

	public void update(Long id, String newPosition) throws SQLException {
		String query = String.format("UPDATE %s SET position = ? WHERE id = ?", TABLE_NAME);
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setString(1, newPosition);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		}
	}

	public Optional<PieceDto> findById(Long id) throws SQLException {
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
			return mapPieceDto(rs);
		}
	}

	public List<Piece> findAllByGameId(Long gameId) throws SQLException {
		String query = String.format("SELECT * FROM %s WHERE game_id = ?", TABLE_NAME);
		List<Piece> pieces = new ArrayList<>();
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setLong(1, gameId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String symbol = rs.getString("symbol");
				String position = rs.getString("position");
				Piece piece = PieceConverter.of(symbol, position);
				pieces.add(piece);
			}
		}
		return pieces;
	}

	public Optional<PieceDto> findByGameIdAndPosition(Long gameId, String position) throws SQLException {
		String query = String.format("SELECT * FROM %s WHERE game_id = ? AND position = ?", TABLE_NAME);

		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setLong(1, gameId);
			pstmt.setString(2, position);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				return Optional.empty();
			}
			return mapPieceDto(rs);
		}
	}

	private Optional<PieceDto> mapPieceDto(ResultSet rs) throws SQLException {
		return Optional.of(new PieceDto(
			rs.getLong("id"),
			rs.getLong("game_id"),
			rs.getString("symbol"),
			rs.getString("team"),
			rs.getString("position"))
		);
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
