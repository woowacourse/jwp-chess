package wooteco.chess.domain.chesspiece;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Positions;

public class QueenTest {

	@DisplayName("못 가는 경우")
	@Test
	void makePathTest1() {
		Piece queen = new Queen(Position.of(4, 4), Team.WHITE);

		assertThatThrownBy(() -> queen.makePathAndValidate(new Blank(Position.of(2, 5))))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("갈 수 있는 경우")
	@Test
	void makePathTest2() {
		Piece queen = new Queen(Position.of(4, 4), Team.WHITE);
		Positions actualPositions = queen.makePathAndValidate(new Blank(Position.of(1, 7)));
		Positions expectedPositions = new Positions(
			Arrays.asList(Position.of(3, 5), Position.of(2, 6)));

		assertThat(actualPositions).isEqualTo(expectedPositions);
	}
}
