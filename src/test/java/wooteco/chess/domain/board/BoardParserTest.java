package wooteco.chess.domain.board;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.piece.Team.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

class BoardParserTest {

	@DisplayName("문자열 타입을 Map<Position, Piece> 객체로 변환")
	@Test
	void parsePieces() {
		String input =
			"RNBQKBNR\n" +
				"PPPPPPPP\n" +
				"........\n" +
				"........\n" +
				"........\n" +
				"........\n" +
				"pppppppp\n" +
				"rnbqkbnr\n";
		Map<Position, Piece> pieces = BoardParser.parsePieces(input);
		assertThat(pieces).contains(
			entry(Position.of("a2"), new Pawn(WHITE)),
			entry(Position.of("b2"), new Pawn(WHITE)),
			entry(Position.of("c2"), new Pawn(WHITE)),
			entry(Position.of("d2"), new Pawn(WHITE)),
			entry(Position.of("e2"), new Pawn(WHITE)),
			entry(Position.of("f2"), new Pawn(WHITE)),
			entry(Position.of("g2"), new Pawn(WHITE)),
			entry(Position.of("h2"), new Pawn(WHITE))
		);
	}

	@DisplayName("보드 객체를 문자열 타입으로 변환")
	@Test
	void parseString() {
		String expected =
			"RNBQKBNR\n" +
				"PPPPPPPP\n" +
				"........\n" +
				"........\n" +
				"........\n" +
				"........\n" +
				"pppppppp\n" +
				"rnbqkbnr\n";

		Board board = BoardFactory.create(expected);
		String actual = BoardParser.parseString(board);
		assertThat(actual).isEqualTo(expected);
	}
}