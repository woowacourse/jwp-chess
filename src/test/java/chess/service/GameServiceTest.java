package chess.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import chess.configuration.RepositoryConfiguration;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.web.dto.PieceDto;
import chess.web.dto.RoomDto;

@SpringBootTest
@Import(RepositoryConfiguration.class)
class GameServiceTest {

	private static final String testName = "summer";
	private static final String testPassword = "summer";

	@Autowired
	private RoomService roomService;
	@Autowired
	private GameService gameService;
	private int roomId;

	@BeforeEach
	void init() {
		roomId = (int) roomService.create(new RoomDto(testName, testPassword)).getId();
	}

	@AfterEach
	void clear() {
		roomService.delete(roomId, testPassword);
	}

	@Test
	@DisplayName("새 게임을 시작한다.")
	void startNewGame() {
		BoardDto board = gameService.startNewGame(roomId);
		assertThat(board.getBoardId()).isNotNull();
	}

	@Test
	@DisplayName("체스말을 움직인다.")
	void move() {
		BoardDto boardDto = gameService.startNewGame(roomId);
		gameService.move(boardDto.getBoardId(), new CommendDto("h2", "h3"));
		BoardDto movedBoard = gameService.loadGame(roomId);

		Optional<PieceDto> findPiece = movedBoard.getPieces().stream()
			.filter(piece -> piece.getPosition().equals("h3"))
			.findAny();
		assertThat(findPiece).isPresent();
	}
}