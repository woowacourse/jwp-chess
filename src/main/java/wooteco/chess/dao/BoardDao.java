package wooteco.chess.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.PieceDto;

@Repository("BoardDao")
public class BoardDao {
	private static final String INSERT_PIECE = "INSERT INTO board (position, name) VALUES (?, ?)";
	private static final String TRUNCATE_BOARD = "TRUNCATE board";
	private static final String UPDATE_BOARD_SET_NAME_WHERE_POSITION = "UPDATE board SET name = (?) WHERE position = (?)";
	private static final String SELECT_FROM_BOARD = "SELECT * FROM board";

	public List<PieceDto> findAll() {
		return Connector.executeQuery(SELECT_FROM_BOARD, new PieceRowMapper());
	}

	public void addPiece(PieceDto pieceDto) {
		Connector.executeUpdate(INSERT_PIECE, pieceDto.getPosition(), pieceDto.getName());
	}

	public void removeAll() {
		Connector.executeUpdate(TRUNCATE_BOARD);
	}

	public void update(Position position, String name) {
		Connector.executeUpdate(UPDATE_BOARD_SET_NAME_WHERE_POSITION, name, position.getString());
	}

}
