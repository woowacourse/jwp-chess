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

class QueenTest {
	static Stream<Arguments> generatePositions() {
		return Stream.of(
			Arguments.of(D1, B1, true),
			Arguments.of(D1, B3, true),
			Arguments.of(D1, A4, false),
			Arguments.of(D1, A2, false),
			Arguments.of(D1, F1, false),//
			Arguments.of(D1, F3, false),
			Arguments.of(D1, E1, false),//
			Arguments.of(D1, H1, false)//
		);
	}

	@ParameterizedTest
	@MethodSource("generatePositions")
	void findMovablePositionsTest(Position currentPosition, Position destination, boolean expect) throws SQLException {
		Map<Position, Piece> pieces = new HashMap<>();
		pieces.put(B1, new Bishop(Color.BLACK));
		pieces.put(B3, new Bishop(Color.BLACK));
		pieces.put(F3, new Bishop(Color.WHITE));
		pieces.put(D1, new Queen(Color.WHITE));
		pieces.put(E1, new King(Color.WHITE));
		Board board = new Board(pieces);
		Piece queen = board.findPieceBy(currentPosition);

		Set<Position> positions = queen.findMovablePositions(currentPosition, board.getPieces());
		assertThat(positions.contains(destination)).isEqualTo(expect);
	}
}