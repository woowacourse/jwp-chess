package wooteco.chess.repository;

import static org.assertj.core.api.Assertions.*;

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

	@Test
	public void CrudTest() {
		chessGameRepository.save(
			new ChessGame("rnbqkbnrppp.pppp....................p...........PPPP.PPPRNBQKBNR", "WHITE", 123456));

		ChessGame game = chessGameRepository.findChessGameByRoomNo(123456);

		assertThat(game.getBoard()).isEqualTo("rnbqkbnrppp.pppp....................p...........PPPP.PPPRNBQKBNR");

		chessGameRepository.updateChessGame("................................................PPPP.PPPRNBQKBNR", "BLACK",
			123456);

		game = chessGameRepository.findChessGameByRoomNo(123456);
		assertThat(game.getBoard()).isEqualTo("................................................PPPP.PPPRNBQKBNR");

		chessGameRepository.deleteByRoomNo(123456);

		game = chessGameRepository.findChessGameByRoomNo(123456);
		assertThat(game).isNull();
	}
}