package wooteco.chess;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChessApplicationTests {

	@Test
	void contextLoads() {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
	}

}
