package wooteco.chess.domain.piece;

import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.PositionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PieceTest {

	private Position position;
	private Piece piece;

	@BeforeEach
	void setUp() {
		position = PositionFactory.of("a4");
		piece = TestPieceFactory.createKing(position, Color.WHITE);
	}

	@DisplayName("move piece 원래 position으로 이동하려하면 예외처리")
	@Test
	void move_when_same_position_throw_exception() {
		assertThatThrownBy(() -> piece.move(position))
				.isInstanceOf(UnsupportedOperationException.class)
				.hasMessage("같은 위치로 이동할 수 없습니다.");
	}
}