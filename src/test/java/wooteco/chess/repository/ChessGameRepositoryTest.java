package wooteco.chess.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.entity.ChessGame;

@SpringBootTest
class ChessGameRepositoryTest {

	@Autowired
	private ChessGameRepository chessGameRepository;

	@Test
	@DisplayName("Create 테스트")
	public void createTest() {
		chessGameRepository.save(
			new ChessGame("rnbqkbnrppp.pppp....................p...........PPPP.PPPRNBQKBNR", "WHITE", 123456));
	}
}