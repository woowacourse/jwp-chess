package wooteco.chess;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.dao.InMemoryChessGameDao;

@SpringBootTest(classes = {InMemoryChessGameDao.class})
class ChessApplicationTests {
	@Test
	void contextLoads() {
	}
}
