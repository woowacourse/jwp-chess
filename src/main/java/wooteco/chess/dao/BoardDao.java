package wooteco.chess.dao;

import wooteco.chess.dao.util.BoardMapper;
import wooteco.chess.dao.util.ConnectionLoader;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.PieceDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {
	public Board save(int roomId, Board board) throws SQLException {
		List<PieceDto> mappers = BoardMapper.convertToPieceDto(board);
		String query = "insert into board(room_id, piece_name, piece_team, piece_position) values (?,?,?,?)";
		try (Connection con = ConnectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			for (PieceDto mapper : mappers) {
				pstmt.setInt(1, roomId);
				pstmt.setString(2, mapper.getSymbol());
				pstmt.setString(3, mapper.getTeam());
				pstmt.setString(4, mapper.getPosition().getName());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}
		return board;
	}

	public Board findByRoomId(int roomId) throws SQLException {
		String query = "select * from board where room_id = (?)";
		try (Connection con = ConnectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, roomId);
			try (ResultSet rs = pstmt.executeQuery()) {
				List<PieceDto> mappers = new ArrayList<>();
				while (rs.next()) {
					mappers.add(new PieceDto(
							Position.of(rs.getString("piece_position")),
							rs.getString("piece_team"),
							rs.getString("piece_name")));
				}
				return BoardMapper.convertToBoard(mappers);
			}
		}
	}

	public void updateBoard(int roomId, Position position, Piece piece) throws SQLException {
		String query = "update board set piece_name = ? , piece_team = ? where room_id = ? and piece_position = ?";
		try (Connection con = ConnectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, piece.getSymbol());
			pstmt.setString(2, piece.getTeam().name());
			pstmt.setInt(3, roomId);
			pstmt.setString(4, position.getName());
			pstmt.executeUpdate();
		}
	}
}
