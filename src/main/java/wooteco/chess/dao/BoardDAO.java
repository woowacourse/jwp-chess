package wooteco.chess.dao;

import java.util.List;

import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.PieceDTO;

public class BoardDAO {
	private static final String INSERT_PIECE = "INSERT INTO board (position, name) VALUES (?, ?)";
	private static final String TRUNCATE_BOARD = "TRUNCATE board";
	private static final String UPDATE_BOARD_SET_NAME_WHERE_POSITION = "UPDATE board SET name = (?) WHERE position = (?)";
	private static final String SELECT_FROM_BOARD = "SELECT * FROM board";

	public List<PieceDTO> findAll() {
		return Connector.executeQuery(SELECT_FROM_BOARD, new PieceRowMapper());
	}

	public void addPiece(PieceDTO pieceDTO) {
		Connector.executeUpdate(INSERT_PIECE, pieceDTO.getPosition(), pieceDTO.getName());
	}

	public void removeAll() {
		Connector.executeUpdate(TRUNCATE_BOARD);
	}

	public void update(Position position, String name) {
		Connector.executeUpdate(UPDATE_BOARD_SET_NAME_WHERE_POSITION, name, position.toString());
	}

}
