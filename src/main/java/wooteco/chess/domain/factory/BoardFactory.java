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
import wooteco.chess.dto.PieceDto;

public class BoardFactory {
	private static final String NOT_MATCH_ALL_PIECE_NUMBER_MESSAGE = "64개의 ChessDTO가 아닙니다.";
	private static final String NOT_MATCH_POSITION_MESSAGE = "찾을 수 없는 포지션입니다.";
	private static final String POSITION_FORMAT = "%c%d";

	public static Board createBoard() {
		List<Row> board = new ArrayList<>();
		board.addAll(createBlackTeam());
		board.addAll(createBlankTeam());
		board.addAll(createWhiteTeam());
		return new Board(board, new Turn(true));
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

	public static Board createBoard(List<PieceDto> pieceDtos, Turn turn) {
		if (pieceDtos.size() != ALL_PIECE_NUMBER.get()) {
			throw new IllegalArgumentException(NOT_MATCH_ALL_PIECE_NUMBER_MESSAGE);
		}
		List<Row> rows = new ArrayList<>();
		for (int x = BOARD_FROM_INDEX.get(); x <= BOARD_TO_INDEX.get(); x++) {
			rows.add(createRow(pieceDtos, x));
		}
		return new Board(rows, turn);
	}

	private static Row createRow(List<PieceDto> pieceDtos, int x) {
		List<Piece> pieces = new ArrayList<>();
		for (int y = ROW_FROM_INDEX.get(); y <= ROW_TO_INDEX.get(); y++) {
			PieceDto pieceDto = findByPosition(pieceDtos, String.format(POSITION_FORMAT, y, x));
			String name = pieceDto.getName();
			String position = pieceDto.getPosition();
			pieces.add(PieceConverter.convert(position, name));
		}
		return new Row(pieces);
	}

	private static PieceDto findByPosition(List<PieceDto> pieceDtos, String position) {
		return pieceDtos.stream()
			.filter(pieceDto -> pieceDto.getPosition().equals(position))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(NOT_MATCH_POSITION_MESSAGE));
	}
}
