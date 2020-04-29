package wooteco.chess.domain.factory;

import static wooteco.chess.domain.factory.BoardIndex.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chessboard.Row;
import wooteco.chess.domain.chesspiece.Bishop;
import wooteco.chess.domain.chesspiece.Blank;
import wooteco.chess.domain.chesspiece.King;
import wooteco.chess.domain.chesspiece.Knight;
import wooteco.chess.domain.chesspiece.Pawn;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.chesspiece.Queen;
import wooteco.chess.domain.chesspiece.Rook;
import wooteco.chess.domain.position.Position;

public class BoardFactory {

	public static Board create() {
		List<Row> board = new ArrayList<>();
		board.addAll(createBlackTeam());
		board.addAll(createBlankTeam());
		board.addAll(createWhiteTeam());
		return new Board(board, new Turn(Turn.FIRST));
	}

	private static List<Row> createBlackTeam() {
		return Arrays.asList(
			createExecutive(BLACK_TEAM_EXECUTIVE_INDEX.get(), Team.BLACK),
			createPawns(BLACK_TEAM_PAWN_INDEX.get(), Team.BLACK));
	}

	private static List<Row> createBlankTeam() {
		List<Row> rows = new ArrayList<>();
		for (int index = BLACK_TO_INDEX.get(); index >= BLACK_FROM_INDEX.get(); index--) {
			rows.add(createBlanks(index));
		}
		return rows;
	}

	private static List<Row> createWhiteTeam() {
		return Arrays.asList(
			createPawns(WHITE_TEAM_PAWN_INDEX.get(), Team.WHITE),
			createExecutive(WHITE_TEAM_EXECUTIVE_INDEX.get(), Team.WHITE));
	}

	private static Row createExecutive(int index, Team team) {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(Position.of(index, ROOK_FIRST_INDEX.get()), team));
		pieces.add(new Knight(Position.of(index, KNIGHT_FIRST_INDEX.get()), team));
		pieces.add(new Bishop(Position.of(index, BISHOP_FIRST_INDEX.get()), team));
		pieces.add(new Queen(Position.of(index, QUEEN_INDEX.get()), team));
		pieces.add(new King(Position.of(index, KING_INDEX.get()), team));
		pieces.add(new Bishop(Position.of(index, BISHOP_SECOND_INDEX.get()), team));
		pieces.add(new Knight(Position.of(index, KNIGHT_SECOND_INDEX.get()), team));
		pieces.add(new Rook(Position.of(index, ROOK_SECOND_INDEX.get()), team));
		return new Row(pieces);
	}

	private static Row createPawns(int index, Team team) {
		List<Piece> pieces = new ArrayList<>();
		for (int y = BOARD_FROM_INDEX.get(); y <= BOARD_TO_INDEX.get(); y++) {
			pieces.add(new Pawn(Position.of(index, y), team));
		}
		return new Row(pieces);
	}

	private static Row createBlanks(int index) {
		List<Piece> pieces = new ArrayList<>();
		for (int y = BOARD_FROM_INDEX.get(); y <= BOARD_TO_INDEX.get(); y++) {
			pieces.add(new Blank(Position.of(index, y)));
		}
		return new Row(pieces);
	}
}
