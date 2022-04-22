package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.state.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessGameDaoTest {

	private ChessGameDao chessGameDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void setup() {
		chessGameDao = new ChessGameDao(jdbcTemplate);
	}

	@Test
	@DisplayName("체스 게임 생성")
	void createChessGame() {
		Turn turn = Turn.WHITE_TURN;

		assertThat(chessGameDao.createChessGame(turn)).isEqualTo(1L);
	}

	@Test
	@DisplayName("현재 게임 상태 반환")
	void findChessGame() {
		Turn turn = Turn.WHITE_TURN;
		long id = chessGameDao.createChessGame(turn);

		assertThat(chessGameDao.findChessGame(id)).isEqualTo(turn);
	}
}
