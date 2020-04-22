package wooteco.chess.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import wooteco.chess.domain.chesspiece.Pawn;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;

public class ScoreTest {

	@Test
	void ofTest() {
		Piece piece = new Pawn(Position.of(1, 1), Team.WHITE);

		assertThat(Score.of(piece)).isEqualTo(Score.PAWN_SCORE);
	}

	@Test
	void sumTest() {
		List<Score> scores = Arrays.asList(Score.values());

		assertThat(Score.sum(scores)).isEqualTo(21);
	}

	@Test
	void calculateDuplicatePawnScoreTest() {
		assertThat(Score.calculateDuplicatePawnScore(4)).isEqualTo(2.0);
	}
}
