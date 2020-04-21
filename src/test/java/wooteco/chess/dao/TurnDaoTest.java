package wooteco.chess.dao;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import wooteco.chess.dto.TurnDto;

class TurnDaoTest {
	private TurnDao turnDao = TurnDao.getInstance();

	@AfterEach
	void tearDown() throws SQLException {
		turnDao.deleteAll();
	}

	@Test
	void addPiece() throws SQLException {
		TurnDto turnDto = new TurnDto("black");
		turnDao.add(turnDto);
	}

	@Test
	void find() throws SQLException {
		turnDao.find();
	}

	@Test
	void update() throws SQLException {
		TurnDto turnDto = new TurnDto("white");
		turnDao.update(turnDto);
	}
}
