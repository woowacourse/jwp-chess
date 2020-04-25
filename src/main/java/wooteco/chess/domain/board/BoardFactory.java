package wooteco.chess.domain.board;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

public class BoardFactory {
	private static final Pattern PIECES_PATTERN = Pattern.compile("([\\.pPbBrRkKnNqQ]{8}\\n){8}");
	private static final String ILLEGAL_BOARD_REGEX_EXCEPTION_MESSAGE = "문자열의 형태가 체스판의 형식이 아닙니다.";

	public static Board create(String board) {
		validateBoardRegex(Objects.requireNonNull(board));
		Map<Position, Piece> pieces = BoardParser.parsePieces(board);
		return new Board(pieces);
	}

	private static void validateBoardRegex(String boards) {
		if (!PIECES_PATTERN.matcher(boards).matches()) {
			throw new IllegalArgumentException(String.format(ILLEGAL_BOARD_REGEX_EXCEPTION_MESSAGE + "%s", boards));
		}
	}
}
