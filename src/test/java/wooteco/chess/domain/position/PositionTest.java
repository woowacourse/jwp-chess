package wooteco.chess.domain.position;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PositionTest {
	@Test
	void name() {
		Position position = Position.of(8, 8);
		assertThat(position.equals(Position.of("h8"))).isTrue();
	}
}