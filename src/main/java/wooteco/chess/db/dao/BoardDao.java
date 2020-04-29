package wooteco.chess.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import wooteco.chess.db.BoardMapper;
import wooteco.chess.db.ConnectionLoader;
import wooteco.chess.db.entity.BoardEntity;
import wooteco.chess.domain.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

@Component
public class BoardDao {
	private final ConnectionLoader connectionLoader;

	public BoardDao(ConnectionLoader connectionLoader) {
		this.connectionLoader = connectionLoader;
	}

	public Board create(int roomId, Board board) throws SQLException {
		List<BoardEntity> mappers = BoardMapper.createMappers(board);
		String query = "insert into board(room_id, piece_name, piece_team, piece_position) values (?,?,?,?)";
		try (Connection con = connectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			for (BoardEntity mapper : mappers) {
				pstmt.setInt(1, roomId);
				pstmt.setString(2, mapper.getPieceName());
				pstmt.setString(3, mapper.getPieceTeam());
				pstmt.setString(4, mapper.getPiecePosition());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}
		return board;
	}

	public Board findByRoomId(int roomId) throws SQLException {
		String query = "select * from board where room_id = (?)";
		try (Connection con = connectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, roomId);
			return createResult(pstmt);
		}
	}

	public void updateBoard(int roomId, Position position, Piece piece) throws SQLException {
		String query = "update board set piece_name = ? , piece_team = ? where room_id = ? and piece_position = ?";
		try (Connection con = connectionLoader.load(); PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, piece.getSymbol());
			pstmt.setString(2, piece.getTeam().name());
			pstmt.setInt(3, roomId);
			pstmt.setString(4, position.getName());
			pstmt.executeUpdate();
		}
	}

	private Board createResult(PreparedStatement pstmt) throws SQLException {
		try (ResultSet rs = pstmt.executeQuery()) {
			List<BoardEntity> mappers = new ArrayList<>();
			while (rs.next()) {
				mappers.add(new BoardEntity(rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			return BoardMapper.create(mappers);
		}
	}
}
