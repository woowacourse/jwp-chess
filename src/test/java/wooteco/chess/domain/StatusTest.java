package wooteco.chess.domain;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.position.Fixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.Turn;

class StatusTest {

	@Test
	void result() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Turn.BLACK));

		Map<Turn, Double> expected = new HashMap<>();
		expected.put(Turn.BLACK, 5.0);
		expected.put(Turn.WHITE, 0.0);

		assertThat(Status.of(pieces).toMap()).isEqualTo(expected);
	}

	@Test
	void winner_Return_White() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Turn.WHITE));

		assertThat(Status.of(pieces).getWinner()).isEqualTo(Turn.WHITE);
	}

	@Test
	void winner_Return_Black() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Turn.BLACK));

		assertThat(Status.of(pieces).getWinner()).isEqualTo(Turn.BLACK);
	}

	@Test
	void winner_Return_Draw() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Turn.BLACK));
		pieces.add(new Rook(A3, Turn.WHITE));

		assertThat(Status.of(pieces).getWinner()).isEqualTo(Turn.NONE);
	}
}