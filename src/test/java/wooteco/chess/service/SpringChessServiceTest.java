package wooteco.chess.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import wooteco.chess.db.ChessPieceRepository;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
@SpringBootTest
public class SpringChessServiceTest {
	@Autowired
	private ChessPieceRepository chessPieceRepository;

	@DisplayName("DB에 있는 room 이름을 받아오는 기능 확인")
	@Test
	void getRoomNamesTest() {
		chessPieceRepository.savePiece("test:3ali7m", "a2", "p");
		assertTrue(chessPieceRepository.findRoomNames().contains("test:3ali7m"));
		chessPieceRepository.deleteById("test:3ali7m");
	}
}
