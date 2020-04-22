package wooteco.chess.domain.piece;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.position.PositionFixture.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.position.Position;

class RookTest {
	static Stream<Arguments> generatePositions() {
		return Stream.of(
			Arguments.of(A1, A3, true),
			Arguments.of(A1, C1, true),
			Arguments.of(A1, B2, false),
			Arguments.of(A1, B3, false),
			Arguments.of(A1, A1, false),
			Arguments.of(A1, A8, true), //먹는것
			Arguments.of(A1, H1, false)
		);
	}

	@ParameterizedTest
	@MethodSource("generatePositions")
	void findMovablePositionsTest(Position currentPosition, Position destination, boolean expect) throws SQLException {
		Map<Position, Piece> pieces = new HashMap<>();
		pieces.put(A1, new Rook(Color.WHITE));
		pieces.put(A8, new Rook(Color.BLACK));
		pieces.put(H1, new Rook(Color.WHITE));
		Board board = new Board(pieces);

		Piece rook = board.findPieceBy(currentPosition);

		Set<Position> positions = rook.findMovablePositions(currentPosition, board.getPieces());
		assertThat(positions.contains(destination)).isEqualTo(expect);
	}
}
