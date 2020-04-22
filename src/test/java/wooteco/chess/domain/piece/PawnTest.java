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
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.position.Position;

class PawnTest {
	static Stream<Arguments> generatePositions() {
		return Stream.of(
			Arguments.of(B2, A3, true),
			Arguments.of(B2, C3, false),
			Arguments.of(A2, A3, false),
			Arguments.of(B2, B3, true),
			Arguments.of(C2, C3, false),
			Arguments.of(C2, C4, false),
			Arguments.of(A2, B3, false),
			Arguments.of(D2, D4, true)
		);
	}

	@ParameterizedTest
	@MethodSource("generatePositions")
	void findMovablePositionsTest(Position currentPosition, Position destination, boolean expect) throws SQLException {
		Map<Position, Piece> pieces = new HashMap<>();
		pieces.put(A3, new Bishop(Color.BLACK));
		pieces.put(C3, new Bishop(Color.WHITE));
		Board board = new Board(BoardFactory.initializePawn(pieces));
		Piece pawn = board.findPieceBy(currentPosition);

		Set<Position> positions = pawn.findMovablePositions(currentPosition, board.getPieces());
		assertThat(positions.contains(destination)).isEqualTo(expect);
	}
}