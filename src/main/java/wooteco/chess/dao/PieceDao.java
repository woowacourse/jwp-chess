package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.dto.PieceDto;
import wooteco.chess.util.PieceConverter;

@Repository
public class PieceDao {
	private static final PieceDao PIECE_DAO = new PieceDao();
	private static final String TABLE_NAME = "PIECE";

	protected PieceDao() {
	}

	public static PieceDao getInstance() {
		return PIECE_DAO;
	}

	public void save(PieceDto pieceDto) {
		String query = String.format("INSERT INTO %s (SYMBOL,GAME_ID,POSITION,TEAM) VALUES(?,?,?,?)", TABLE_NAME);
		try (
			Connection conn = DBConnector.getMysqlConnection();
			PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
		) {
			pstmt.setString(1, pieceDto.getSymbol());
			pstmt.setLong(2, pieceDto.getGameId());
			pstmt.setString(3, pieceDto.getPosition());
			pstmt.setString(4, pieceDto.getTeam());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLAccessException(e.getMessage());
		}
	}

	public void update(Long id, String newPosition) {
		String query = String.format("UPDATE %s SET position = ? WHERE id = ?", TABLE_NAME);
		try (
			Connection conn = DBConnector.getMysqlConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setString(1, newPosition);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLAccessException(e.getMessage());
		}
	}

	public List<Piece> findAllByGameId(Long gameId) {
		String query = String.format("SELECT * FROM %s WHERE game_id = ?", TABLE_NAME);
		List<Piece> pieces = new ArrayList<>();
		try (
			Connection conn = DBConnector.getMysqlConnection();
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
		} catch (SQLException e) {
			throw new SQLAccessException(e.getMessage());
		}
		return pieces;
	}

	public Optional<PieceDto> findByGameIdAndPosition(Long gameId, String position) {
		String query = String.format("SELECT * FROM %s WHERE game_id = ? AND position = ?", TABLE_NAME);
		try (
			Connection conn = DBConnector.getMysqlConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setLong(1, gameId);
			pstmt.setString(2, position);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				return Optional.empty();
			}
			return mapPieceDto(rs);
		} catch (SQLException e) {
			throw new SQLAccessException(e.getMessage());
		}
	}

	private Optional<PieceDto> mapPieceDto(ResultSet rs) {
		Optional<PieceDto> pieceDto;
		try {
			pieceDto = Optional.of(new PieceDto(
				rs.getLong("id"),
				rs.getLong("game_id"),
				rs.getString("symbol"),
				rs.getString("team"),
				rs.getString("position"))
			);
		} catch (SQLException e) {
			throw new SQLAccessException();
		}
		return pieceDto;
	}

	public void deleteAll() {
		String query = String.format("DELETE FROM %s", TABLE_NAME);
		try (
			Connection conn = DBConnector.getMysqlConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLAccessException(e.getMessage());
		}
	}

	public void deleteByGameIdAndPosition(Long gameId, String position) {
		String query = String.format("DELETE FROM %s WHERE game_id = ? AND position = ?", TABLE_NAME);
		try (
			Connection conn = DBConnector.getMysqlConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setLong(1, gameId);
			pstmt.setString(2, position);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLAccessException();
		}
	}
}
