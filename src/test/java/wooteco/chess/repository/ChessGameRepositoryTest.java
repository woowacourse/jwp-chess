package wooteco.chess.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import wooteco.chess.entity.ChessGame;
import wooteco.chess.exceptions.RoomNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
class ChessGameRepositoryTest {

	@Autowired
	private ChessGameRepository chessGameRepository;

	@BeforeEach
	public void resetDatabase() {
		chessGameRepository.deleteAll();
	}

	@Test
	public void CrudTest() {
		chessGameRepository.save(
			new ChessGame("rnbqkbnrppp.pppp....................p...........PPPP.PPPRNBQKBNR", "WHITE", 123456));

		ChessGame game = chessGameRepository.findChessGameByRoomNo(123456)
			.orElseThrow(RoomNotFoundException::new);

		assertThat(game.getBoard()).isEqualTo("rnbqkbnrppp.pppp....................p...........PPPP.PPPRNBQKBNR");

		game.setBoard("................................................PPPP.PPPRNBQKBNR");
		game.setTurn("BLACK");
		chessGameRepository.save(game);

		game = chessGameRepository.findChessGameByRoomNo(123456)
				.orElseThrow(RoomNotFoundException::new);
		assertThat(game.getBoard()).isEqualTo("................................................PPPP.PPPRNBQKBNR");

		chessGameRepository.delete(game);

		assertThat(chessGameRepository.findChessGameByRoomNo(123456)).isEqualTo(Optional.empty());
	}
}