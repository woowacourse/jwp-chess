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
import wooteco.chess.domain.piece.Team;

class StatusTest {

	@Test
	void result() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Team.BLACK));

		Map<Team, Double> expected = new HashMap<>();
		expected.put(Team.BLACK, 5.0);
		expected.put(Team.WHITE, 0.0);

		assertThat(Status.of(pieces).toMap()).isEqualTo(expected);
	}

	@Test
	void winner_Return_White() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Team.WHITE));

		assertThat(Status.of(pieces).getWinner()).isEqualTo(Team.WHITE);
	}

	@Test
	void winner_Return_Black() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Team.BLACK));

		assertThat(Status.of(pieces).getWinner()).isEqualTo(Team.BLACK);
	}

	@Test
	void winner_Return_Draw() {
		List<Piece> pieces = new ArrayList<>();
		pieces.add(new Rook(A2, Team.BLACK));
		pieces.add(new Rook(A3, Team.WHITE));

		assertThat(Status.of(pieces).getWinner()).isEqualTo(Team.NONE);
	}
}