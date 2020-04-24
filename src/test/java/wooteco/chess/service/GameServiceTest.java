package wooteco.chess.service;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.chess.repository.BlackKingCatchedGameDao;
import wooteco.chess.repository.FinishedDrawGameDao;
import wooteco.chess.repository.GameDao;
import wooteco.chess.repository.ReadyGameDao;
import wooteco.chess.repository.StartedGameDao;
import wooteco.chess.repository.WhiteMoreScoreGameDao;
import wooteco.chess.view.dto.requestdto.PositionRequestDto;
import wooteco.chess.view.dto.responsedto.BoardDto;
import wooteco.chess.view.dto.responsedto.GameDto;
import wooteco.chess.view.dto.responsedto.ScoreDto;
import wooteco.chess.view.response.ResponseStatus;
import wooteco.chess.view.response.ResponseDto;

class GameServiceTest {
	private GameService service;
	private GameDao gameDAO;

	private static Stream<Arguments> gameStateSet() {
		return Stream.of(
			Arguments.of(new ReadyGameDao(), new PositionRequestDto("a1", "a3")),
			Arguments.of(new FinishedDrawGameDao(), new PositionRequestDto("a1", "a3"))
		);
	}

	private static Stream<Arguments> initialPositionSet() {
		return Stream.of(
			Arguments.arguments(new PositionRequestDto("a1", "a2"),
				asList(new BoardDto("a1", "r"),
					new BoardDto("a2", "p"))
			),
			Arguments.arguments(new PositionRequestDto("c1", "c5"),
				asList(new BoardDto("c1", "b"),
					new BoardDto("c5", "."))
			),
			Arguments.arguments(new PositionRequestDto("b1", "b8"),
				asList(new BoardDto("b1", "n"),
					new BoardDto("b8", "N"))
			)
		);
	}

	private static Stream<Arguments> stateAndIsNotFinishExpectedSets() {
		return Stream.of(
			Arguments.of(new ReadyGameDao(), true),
			Arguments.of(new StartedGameDao(), true),
			Arguments.of(new FinishedDrawGameDao(), false)
		);
	}

	@DisplayName("게임 시작전 점수 계산 서비스 실행시, Error status 반환")
	@Test
	void calculateScore_Ready_state_exception_test() {
		gameDAO = new ReadyGameDao();
		service = new GameService(gameDAO);
		ResponseDto response = service.calculateScore();
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임중 점수 계산 서비스 실행시, 정상적으로 DTO 리스트 반환")
	@Test
	void calculateScore_Started_state_normal_test() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		assertThat((List<ScoreDto>)service.calculateScore().getData()).containsExactlyInAnyOrder(
			new ScoreDto("black", 38.0),
			new ScoreDto("white", 38.0)
		);
	}

	@DisplayName("게임 종료후 점수 계산 서비스 실행시, 정상적으로 DTO 리스트 반환")
	@Test
	void calculateScore_Finished_state_normal_test() {
		gameDAO = new FinishedDrawGameDao();
		service = new GameService(gameDAO);
		assertThat((List<ScoreDto>)service.calculateScore().getData()).containsExactlyInAnyOrder(
			new ScoreDto("black", 38.0),
			new ScoreDto("white", 38.0)
		);
	}

	@DisplayName("비어있는 보드 판에서 기물 DTO 목록 가져올시, 빈 리스트 반환한다.")
	@Test
	void findAllPiecesOnEmptyBoardTest() {
		gameDAO = new ReadyGameDao();
		service = new GameService(gameDAO);
		assertThat((List<BoardDto>)service.findAllPiecesOnBoard().getData()).isEqualTo(Collections.emptyList());
	}

	@DisplayName("게임 중이 아닌 경우, move 실행시 error status 반환")
	@ParameterizedTest
	@MethodSource("gameStateSet")
	void moveExceptionTest(GameDao notPlayingStateDAO, PositionRequestDto position) {
		service = new GameService(notPlayingStateDAO);
		ResponseDto response = service.move(position);
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임 중인 상태에서 올바른 차례의 말 움직이면 예외 없이 정상 실행")
	@Test
	void startedGameMoveTest() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		assertThatCode(() -> {
			service.move(new PositionRequestDto("a2", "a4"));
			service.move(new PositionRequestDto("a7", "a5"));
		}).doesNotThrowAnyException();
	}

	@DisplayName("게임 중인 상태에서 현재 차례 말을 이동하지 못하는 경로로 움직이면 error status 반환")
	@Test
	void startedGameMoveWrongWayTest() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		service.move(new PositionRequestDto("b2", "b4"));
		service.move(new PositionRequestDto("b7", "b5"));
		ResponseDto response = service.move(new PositionRequestDto("a1", "c3"));
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임 중인 상태에서 현재 차례 말을 이동하려는 경로 사이 장애물이 있는 경우 error status 반환")
	@Test
	void startedGameMoveWithObstacleTest() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		ResponseDto response = service.move(new PositionRequestDto("a1", "a5"));
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임 중인 상태에서 현재 차례가 아닌 말을 움직이면 error status 빈허ㅣ")
	@Test
	void startedGameMoveWrongTurnTest() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		service.move(new PositionRequestDto("a2", "a4"));
		ResponseDto response = service.move(new PositionRequestDto("a4", "a5"));
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임 중인 상태에서 현재 차례가 아닌 말을 움직이면 error status 빈환")
	@Test
	void startedGameMoveWrongTurnTest2() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		ResponseDto response = service.move(new PositionRequestDto("a7", "a5"));
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임 중인 상태에서 빈공간을 출발지로 move 시 error status 반환")
	@Test
	void startedGameMoveEmptySpaceTest() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		ResponseDto response = service.move(new PositionRequestDto("a4", "a5"));
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("초기화된 보드 판에서 기물 DTO 목록 가져올시, 보드위 모든 기물 DTO 목록 반환한다.")
	@Test
	void findAllPiecesOnInitialBoardTest() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		assertThat((List<BoardDto>)service.findAllPiecesOnBoard().getData()).contains(
			new BoardDto("a2", "p"),
			new BoardDto("b2", "p"),
			new BoardDto("c2", "p"),
			new BoardDto("d2", "p"),
			new BoardDto("e2", "p"),
			new BoardDto("f2", "p"),
			new BoardDto("g2", "p"),
			new BoardDto("h2", "p")
		);
	}

	@DisplayName("인자로 받는 두 위치에 해당하는 좌표, 기물 정보가 담긴 DTO 목록을 가져온다.")
	@ParameterizedTest
	@MethodSource("initialPositionSet")
	void findChangedPiecesOnBoardTest(PositionRequestDto positionDTO, List<BoardDto> expected) {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);

		List<BoardDto> actual = service.findChangedPiecesOnBoard(positionDTO);
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("게임 상태 변경이 올바르게 이뤄졌는지 확인한다.")
	@ParameterizedTest
	@CsvSource({"start,started", "end,suspendFinish"})
	void changeStateTest(String request, String expeected) {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		service.changeState(request);
		assertThat(((GameDto)service.getCurrentState().getData()).getGameState()).isEqualTo(expeected);
	}

	@DisplayName("게임이 끝난 상태가 아니면 true 반환한다.")
	@ParameterizedTest
	@MethodSource("stateAndIsNotFinishExpectedSets")
	void isNotFinishTest(GameDao gameDAO, boolean expected) {
		service = new GameService(gameDAO);
		boolean actual = (boolean)service.isNotFinish().getData();
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("게임이 준비 상태인 경우, 승자를 구하고자 할때 error status 반환")
	@Test
	void getWinnerExceptionTest() {
		gameDAO = new ReadyGameDao();
		service = new GameService(gameDAO);
		ResponseDto response = service.getWinner();
		assertThat(response.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임이 진행중인 상태인 경우, 승자를 구하고자 할때 Error status를 반환한다.")
	@Test
	void getWinnerWithStartedStateExceptionTest() {
		gameDAO = new StartedGameDao();
		service = new GameService(gameDAO);
		ResponseDto winner = service.getWinner();
		assertThat(winner.getStatus()).isEqualTo(ResponseStatus.ERROR);
	}

	@DisplayName("게임이 동점으로 끝난 상태인 경우, 승자가 없다.")
	@Test
	void getWinnerWithFinishedStateTest() {
		gameDAO = new FinishedDrawGameDao();
		service = new GameService(gameDAO);
		assertThat((String)service.getWinner().getData()).isEqualTo("none");
	}

	@DisplayName("검정 왕이 잡힌 경우, 하얀 팀이 이긴다.")
	@Test
	void getWinnerWithFinishedStateTest2() {
		gameDAO = new BlackKingCatchedGameDao();
		service = new GameService(gameDAO);
		assertThat((String)service.getWinner().getData()).isEqualTo("white");
	}

	@DisplayName("하얀 팀이 기물 점수가 더 높은 경우, 하얀 팀이 이긴다.")
	@Test
	void getWinnerWithFinishedStateTest3() {
		gameDAO = new WhiteMoreScoreGameDao();
		service = new GameService(gameDAO);
		assertThat((String)service.getWinner().getData()).isEqualTo("white");
	}
}