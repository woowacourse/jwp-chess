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

class KingTest {
	static Stream<Arguments> generatePositions() {
		return Stream.of(
			Arguments.of(E1, D2, true),
			Arguments.of(E1, D1, false),
			Arguments.of(E1, F1, true)
		);
	}

	@ParameterizedTest
	@MethodSource("generatePositions")
	void findMovablePositionsTest(Position currentPosition, Position destination, boolean expect) throws SQLException {
		Map<Position, Piece> pieces = new HashMap<>();
		pieces.put(D2, new Bishop(Color.BLACK));
		pieces.put(E1, new King(Color.WHITE));
		pieces.put(D1, new Queen(Color.WHITE));
		Board board = new Board(pieces);
		Piece king = board.findPieceBy(currentPosition);

		Set<Position> positions = king.findMovablePositions(currentPosition, board.getPieces());
		assertThat(positions.contains(destination)).isEqualTo(expect);
	}
}