package wooteco.chess.domain.state;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.position.Position;

class BoardFactoryTest {
	@Test
	@DisplayName("BoardRepository 가 정상적으로 Board를 생성해주는지 테스트합니다.")
	void createBoardTest() {
		assertThat(BoardFactory.create()).isInstanceOf(Board.class);
	}

	@Test
	void name() {
		System.out.println(Position.of(8, 8).getName());
	}

	@Test
	void name2() {
		final Board board = BoardFactory.create();
		final Map<Position, Piece> board1 = board.getBoard();
		for (Map.Entry<Position, Piece> positionPieceEntry : board1.entrySet()) {
			System.out.println("KEY : "+ positionPieceEntry.getKey().getName() + "VALUE : " + positionPieceEntry.getValue().getSymbol());
		}
	}
}