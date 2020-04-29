package wooteco.chess.entity;

import static wooteco.chess.domain.factory.BoardIndex.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.Turn;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chessboard.Row;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.factory.PieceConverter;

/**
 *    board entity class입니다.
 *
 *    @author HyungJu An, JunSeong Hong
 */
@Table("board")
public class BoardEntity {
	private final Set<PieceEntity> pieces;
	@Id
	private Long board_id;
	private final TurnEntity turn;

	BoardEntity(final Long board_id, final Set<PieceEntity> pieces, final TurnEntity turn) {
		this.board_id = board_id;
		this.pieces = pieces;
		this.turn = turn;
	}

	public static BoardEntity of(final Set<PieceEntity> pieces, final TurnEntity turn) {
		return new BoardEntity(null, pieces, turn);
	}

	public static BoardEntity from(Board board) {
		Set<PieceEntity> pieces = new HashSet<>();
		for (Piece piece : board.findAll()) {
			String position = piece.getPosition().getString();
			String name = piece.getName();
			pieces.add(PieceEntity.of(position, name));
		}
		return new BoardEntity(null, pieces, TurnEntity.of(Turn.FIRST));
	}

	public Board createBoard() {
		List<Row> rows = new ArrayList<>();
		for (int x = BOARD_FROM_INDEX.get(); x <= BOARD_TO_INDEX.get(); x++) {
			rows.add(createRow(x));
		}
		return new Board(rows, turn.createTurn());
	}

	private Row createRow(int x) {
		List<Piece> pieces = new ArrayList<>();
		for (int y = ROW_FROM_INDEX.get(); y <= ROW_TO_INDEX.get(); y++) {
			PieceEntity pieceEntity = findByPosition(String.format("%c%d", y, x));
			String name = pieceEntity.getName();
			String position = pieceEntity.getPosition();
			pieces.add(PieceConverter.convert(position, name));
		}
		return new Row(pieces);
	}

	private PieceEntity findByPosition(String position) {
		return pieces.stream()
			.filter(pieceEntity -> pieceEntity.isMathPosition(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(""));
	}

	public Set<PieceEntity> getPieces() {
		return pieces;
	}
}
