package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.database.dao.BoardDao;
import chess.database.dao.GameDao;
import chess.database.dao.RoomDao;
import chess.database.dto.RoomDto;
import chess.database.dto.RouteDto;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql("/sql/chess-test.sql")
@SpringBootTest
@Transactional
class ChessRoomServiceTest {

    private static final String TEST_ROOM_NAME = "TESTING";
    private static final String TEST_ROOM_PASSWORD = "1234";
    private static final String TEST_CREATION_ROOM_NAME = "TESTING2";
    private static final String TEST_CREATION_ROOM_PASSWORD = "4321";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomDao roomDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private BoardDao boardDao;

    @Autowired
    private ChessRoomService chessRoomService;

    private RoomDto secondRoomDto;
    private RoomDto firstRoomDto;

    @BeforeEach
    void beforeEach() {
        roomDao = new RoomDao(jdbcTemplate);
        gameDao = new GameDao(jdbcTemplate);
        boardDao = new BoardDao(jdbcTemplate);

        firstRoomDto = new RoomDto(TEST_ROOM_NAME, TEST_ROOM_PASSWORD);
        secondRoomDto = new RoomDto(TEST_ROOM_NAME, TEST_ROOM_PASSWORD);
        GameState state = new Ready();
        Board board = Board.of(new InitialBoardGenerator());

        firstRoomDto = roomDao.create(firstRoomDto);
        gameDao.create(firstRoomDto.getId(), state);
        boardDao.saveBoard(firstRoomDto.getId(), board);
    }

    @Test
    @DisplayName("새로운 방을 만든다.")
    public void createNewGame() {
        assertThatCode(() ->
            chessRoomService.createNewRoom(TEST_CREATION_ROOM_NAME, TEST_CREATION_ROOM_PASSWORD))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 방 이름은 중복될 수 없다.")
    public void duplicateRoomException() {
        assertThatThrownBy(() ->
            chessRoomService.createNewRoom(TEST_ROOM_NAME, TEST_ROOM_PASSWORD))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("게임 상태를 얻는다.")
    public void readGameState() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        GameState gameState = chessRoomService.readGameState(roomDto.getId());
        assertThat(gameState).isInstanceOf(Ready.class);
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void startGame() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        assertThatCode(() -> chessRoomService.startGame(roomDto.getId()))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말을 움직인다.")
    public void moveBoard() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        RouteDto routeDto = new RouteDto("a2", "a4");
        chessRoomService.startGame(roomDto.getId());
        GameState moved = chessRoomService.moveBoard(roomDto.getId(), routeDto);
        assertThat(moved).isNotNull();
    }

    @Test
    @DisplayName("게임을 종료한다.")
    public void finishGame() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        assertThatCode(() -> chessRoomService.finishGame(roomDto.getId()))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("모든 게임 방을 조회한다.")
    public void findAllRoom() {
        chessRoomService.createNewRoom(TEST_CREATION_ROOM_NAME, TEST_CREATION_ROOM_PASSWORD);
        Map<RoomDto, String> roomStates = chessRoomService.findAllRoomState();
        System.out.println("rooms:  " + roomStates);
        assertThat(roomStates.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게임방을 삭제한다.")
    public void removeRoom() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        assertThatCode(() -> chessRoomService.removeRoom(roomDto))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 삭제 시 틀린 비밀번호를 입력했다면 예외가 발생한다.")
    public void removeRoom_validate_password() {
        assertThatThrownBy(() ->
            chessRoomService.removeRoom(new RoomDto(1, secondRoomDto.getPassword())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("진행 중인 게임을 삭제하면 예외가 발생한다.")
    public void removeRoom_running_game() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        chessRoomService.startGame(roomDto.getId());
        assertThatThrownBy(() ->
            chessRoomService.removeRoom(new RoomDto(1, firstRoomDto.getPassword())))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
