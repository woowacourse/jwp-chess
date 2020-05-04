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
@Table("room")
public class RoomEntity {
	@Id
	private Long id;
	private final String title;
	private final Set<PieceEntity> pieces;
	private final TurnEntity turn;

	private RoomEntity(Long id, String title, Set<PieceEntity> pieces, TurnEntity turn) {
		this.id = id;
		this.title = title;
		this.pieces = pieces;
		this.turn = turn;
	}

	public static RoomEntity of(String title, Set<PieceEntity> pieces, TurnEntity turn) {
		return new RoomEntity(null, title, pieces, turn);
	}

	public static RoomEntity from(String title, Board board) {
		Set<PieceEntity> pieces = new HashSet<>();
		for (Piece piece : board.findAll()) {
			String position = piece.getPosition().getString();
			String name = piece.getName();
			pieces.add(PieceEntity.of(position, name));
		}
		return RoomEntity.of(title, pieces, TurnEntity.of(Turn.FIRST));
	}

	public Board createBoard() {
		List<Row> rows = new ArrayList<>();
		for (int x = BOARD_FROM.index; x <= BOARD_TO.index; x++) {
			rows.add(createRow(x));
		}
		return new Board(rows, turn.createTurn());
	}

	private Row createRow(int x) {
		List<Piece> pieces = new ArrayList<>();
		for (int y = ROW_FROM.index; y <= ROW_TO.index; y++) {
			PieceEntity pieceEntity = findByPosition(String.format("%c%d", y, x));
			String name = pieceEntity.getName();
			String position = pieceEntity.getPosition();
			pieces.add(PieceConverter.convert(position, name));
		}
		return new Row(pieces);
	}

	private PieceEntity findByPosition(String position) {
		return pieces.stream()
			.filter(pieceEntity -> pieceEntity.isMatchPosition(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Position입니다."));
	}
}
