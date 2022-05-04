package chess.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import chess.configuration.FakeBoardDao;
import chess.configuration.FakePieceDao;
import chess.configuration.FakeRoomDao;
import chess.domain.chessgame.ChessGame;
import chess.exception.UserInputException;
import chess.repository.ChessGameRepository;
import chess.repository.RoomDao;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.repository.entity.PieceEntity;

class ChessGameServiceTest {

	private final String testName = "summer";
	private final String password = "summer";

	private RoomDao roomDao;
	private ChessGameService chessGameService;
	private ChessGameRepository chessGameRepository;

	@BeforeEach
	void init() {
		roomDao = new FakeRoomDao();
		chessGameRepository = new ChessGameRepository(roomDao, new FakeBoardDao(), new FakePieceDao());
		chessGameService = new ChessGameService(chessGameRepository);
	}

	@AfterEach
	void deleteCreated() {
		roomDao.findAll()
			.forEach(room -> roomDao.deleteById(room.getId()));
	}

	@Test
	@DisplayName("이름을 받아 체스 게임 방을 생성한다.")
	void create() {
		ChessGame chessGame = chessGameService.create(testName, password);
		assertThat(chessGame.getName()).isEqualTo(testName);
	}

	@Test
	@DisplayName("이미 있는 이름으로 저장하면 예외가 발생한다.")
	void validateDuplicateName() {
		chessGameService.create(testName, password);

		assertThatThrownBy(() -> chessGameService.create(testName, password))
			.isInstanceOf(UserInputException.class);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
	@DisplayName("빈 이름이나 16자 초과 이름이 들어오면 예외가 발생한다.")
	void createException(String name) {
		assertThatThrownBy(() -> chessGameService.create(name, password))
			.isInstanceOf(UserInputException.class);
	}

	@Test
	@DisplayName("없는 id로 방을 조회하면 예외가 발생한다.")
	void validate() {
		assertThatThrownBy(() -> chessGameService.getChessGameById(0))
			.isInstanceOf(UserInputException.class);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " "})
	@DisplayName("빈 비밀번호를 입력하면 예외가 발생한다.")
	void passwordException(String password) {
		assertThatThrownBy(() -> chessGameService.create(testName, password))
			.isInstanceOf(UserInputException.class);
	}

	@Test
	@DisplayName("id와 비밀번호가 맞지 않으면 삭제하지 못한다.")
	void removeExceptionPassword() {
		ChessGame chessGame = chessGameService.create(testName, password);

		assertThatThrownBy(() -> chessGameService.delete(chessGame.getId(), "1234"))
			.isInstanceOf(UserInputException.class);
	}

	@Test
	@DisplayName("새 게임을 시작한다.")
	void startNewGame() {
		ChessGame chessGame = chessGameService.create(testName, password);
		BoardDto board = chessGameService.getBoardDtoByGameId(chessGame.getId());
		assertThat(board.getBoardId()).isNotNull();
	}

	@Test
	@DisplayName("체스말을 움직인다.")
	void move() {
		ChessGame chessGame = chessGameService.create(testName, password);
		int roomId = chessGame.getId();
		int boardId = chessGameService.startNewGame(roomId);
		chessGameService.move(boardId, new CommendDto("h2", "h3"));

		BoardDto boardDto = chessGameService.getBoardDtoByBoardId(boardId);

		Optional<PieceEntity> findPiece = boardDto.getPieces().stream()
			.filter(piece -> piece.getPosition().equals("h3"))
			.findAny();
		assertThat(findPiece).isPresent();
	}

	@Test
	@DisplayName("종료되지 않은 게임의 방은 삭제하지 못한다.")
	void removeExceptionNotEnd() {
		ChessGame chessGame = chessGameService.create(testName, password);
		chessGameService.startNewGame(chessGame.getId());

		assertThatThrownBy(() -> chessGameService.delete(chessGame.getId(), password))
			.isInstanceOf(UserInputException.class);
	}
}