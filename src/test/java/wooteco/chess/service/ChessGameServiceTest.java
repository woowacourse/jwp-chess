package wooteco.chess.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import chess.domain.board.ChessBoard;

@SpringBootTest
class ChessGameServiceTest {

	@Autowired
	private ChessGameService chessGameService;

	@DisplayName("체스보드게임 생성 테스트")
	@Test
	void createNewChessGame() {
		ChessBoard actual = chessGameService.createNewChessGame();
		ChessBoard expected = new ChessBoard();

		assertThat(actual).isEqualTo(expected);
	}
}