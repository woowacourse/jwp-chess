package wooteco.chess.domain.board;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.piece.Team.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

@SuppressWarnings("NonAsciiCharacters")
class BoardFactoryTest {
	@DisplayName("문자열로 체스판 입력 받아 보드 객체 생성")
	@Test
	void constructWithStringTest() {
		String input =
			"RNBQKBNR\n" +
				"PPPPPPPP\n" +
				"........\n" +
				"........\n" +
				"........\n" +
				"........\n" +
				"pppppppp\n" +
				"rnbqkbnr\n";
		Board board = BoardFactory.create(input);
		Map<Position, Piece> pieces = board.getPieces();
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
}