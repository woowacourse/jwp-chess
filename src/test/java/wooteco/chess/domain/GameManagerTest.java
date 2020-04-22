package wooteco.chess.domain;

import static org.assertj.core.api.Assertions.*;

import java.sql.SQLException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;

class GameManagerTest {
	@Test
	void calculateScoreTest() throws SQLException {
		Board board = BoardFactory.create();
		GameManager game = new GameManager(board);
		Map<Color, Double> eachColorScore = game.calculateEachScore();

		assertThat(eachColorScore.get(Color.BLACK)).isEqualTo(38);
		assertThat(eachColorScore.get(Color.WHITE)).isEqualTo(38);
	}

	@Test
	public void test() {

	}
}