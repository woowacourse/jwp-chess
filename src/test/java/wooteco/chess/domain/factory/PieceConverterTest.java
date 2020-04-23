package wooteco.chess.domain.factory;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.chesspiece.Blank;
import wooteco.chess.domain.chesspiece.King;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;

public class PieceConverterTest {

	@DisplayName("King인 경우")
	@Test
	void convertTest() {
		Piece piece = PieceConverter.convert("k", "h2");

		assertThat(piece).isEqualTo(new King(Position.of("h2"), Team.WHITE));
	}

	@DisplayName("BLANK인 경우")
	@Test
	void convertTest2() {
		Piece piece = PieceConverter.convert(".", "h2");

		assertThat(piece).isEqualTo(new Blank(Position.of("h2")));
	}
}
