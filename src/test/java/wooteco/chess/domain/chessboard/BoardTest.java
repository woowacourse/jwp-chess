package wooteco.chess.domain.chessboard;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.Team;
import wooteco.chess.domain.chesspiece.Blank;
import wooteco.chess.domain.chesspiece.Pawn;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.chesspiece.Rook;
import wooteco.chess.domain.factory.BoardFactory;
import wooteco.chess.domain.position.Position;

public class BoardTest {
	private Board board;

	@BeforeEach
	void setUp() {
		board = BoardFactory.createBoard();
	}

	@Test
	void findByPositionTest() {
		Piece rook = board.findByPosition(Position.of(1, 1));
		Piece pawn = board.findByPosition(Position.of(2, 2));

		assertThat(rook).isEqualTo(new Rook(Position.of(1, 1), Team.WHITE));
		assertThat(pawn).isEqualTo(new Pawn(Position.of(2, 2), Team.WHITE));
	}

	@Test
	void moveTest() {
		Position sourcePosition = Position.of(2, 2);
		Position targetPosition = Position.of(3, 2);

		board.move(sourcePosition, targetPosition);

		assertThat(board.findByPosition(sourcePosition)).isEqualTo(new Blank(sourcePosition));
		assertThat(board.findByPosition(targetPosition)).isEqualTo(new Pawn(targetPosition, Team.WHITE));

	}
}
