package chess.database.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.database.dao.spring.RoomDao;
import chess.database.dao.spring.SpringBoardDao;
import chess.database.dao.spring.SpringGameDao;
import chess.database.dto.BoardDto;
import chess.database.dto.GameStateDto;
import chess.database.dto.PointDto;
import chess.database.dto.RoomDto;
import chess.database.dto.RouteDto;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.board.Point;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class BoardDaoTest {

    private static final String TEST_ROOM_NAME = "TESTING";
    private static final String TEST_ROOM_PASSWORD = "1234";
    private static final String TEST_CREATION_ROOM_NAME = "TESTING22";
    private static final String TEST_CREATION_ROOM_PASSWORD = "4321";
    private static GameState state;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RoomDao roomDao;
    private GameDao gameDao;
    private BoardDao boardDao;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate);
        gameDao = new SpringGameDao(jdbcTemplate);
        state = new Ready();

        RoomDto testRoomDto = new RoomDto(TEST_ROOM_NAME, TEST_ROOM_PASSWORD);
        RoomDto testRoomDto2 = new RoomDto(TEST_CREATION_ROOM_NAME, TEST_CREATION_ROOM_PASSWORD);

        testRoomDto = roomDao.create(testRoomDto);
        testRoomDto2 = roomDao.create(testRoomDto2);

        gameDao.create(GameStateDto.of(state), testRoomDto.getId());
        gameDao.create(GameStateDto.of(state), testRoomDto2.getId());

//        gameDao.saveGame(GameStateDto.of(state), TEST_ROOM_NAME);
//        gameDao.saveGame(GameStateDto.of(state), TEST_CREATION_ROOM_NAME);

        boardDao = new SpringBoardDao(jdbcTemplate);
        Board board = Board.of(new InitialBoardGenerator());
//        boardDao.saveBoard(BoardDto.of(board.getPointPieces()), TEST_ROOM_NAME);
        boardDao.saveBoard(BoardDto.of(board.getPointPieces()), testRoomDto.getId());
    }

    @Test
    @DisplayName("말의 위치와 종류를 저장한다.")
    public void insert() {
        // given & when
        Board board = Board.of(new InitialBoardGenerator());
        RoomDto roomDto = roomDao.findByName(TEST_CREATION_ROOM_NAME);
        // then
        assertThatCode(
            () -> boardDao.saveBoard(BoardDto.of(board.getPointPieces()), roomDto.getId()))
            .doesNotThrowAnyException();
        boardDao.removeBoard(roomDao.findByName(TEST_CREATION_ROOM_NAME).getId());
    }

    @Test
    @DisplayName("말의 위치와 종류를 조회한다.")
    public void select() {
        // given
        String roomName = TEST_ROOM_NAME;
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        // when
//        BoardDto boardDto = boardDao.readBoard(roomName);
        BoardDto boardDto = boardDao.readBoard(roomDto.getId());
        // then
        assertThat(boardDto.getPointPieces().size()).isEqualTo(32);
    }

    //TODO: roomDao에서 테스트 해야하는지 고민
//    @Test
//    @DisplayName("존재하지 않는 방을 조회하면 예외를 던진다.")
//    public void selectMissingName() {
//        // given & when
//        String name = "missing";
//        // then
//        assertThatExceptionOfType(IllegalArgumentException.class)
//            .isThrownBy(() -> boardDao.readBoard(name));
//    }

    @Test
    @DisplayName("말의 위치를 움직인다.")
    public void update() {
        // given & when
//        String roomName = TEST_ROOM_NAME;
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        Route route = Route.of(List.of("a2", "a4"));
        // then
        assertThatCode(() -> boardDao.updatePiece(RouteDto.of(route), roomDto.getId()))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말을 삭제한다.")
    public void delete() {
        // given & when
        String roomName = TEST_ROOM_NAME;
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        Point point = Point.of("b2");
        // then
        assertThatCode(() -> boardDao.deletePiece(PointDto.of(point), roomDto.getId()))
            .doesNotThrowAnyException();
    }

    @AfterEach
    void setDown() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        RoomDto roomDto2 = roomDao.findByName(TEST_CREATION_ROOM_NAME);
        boardDao.removeBoard(roomDto.getId());

        gameDao.removeGame(roomDto.getId());
        gameDao.removeGame(roomDto2.getId());

        roomDao.delete(roomDto);
        roomDao.delete(roomDto2);
    }
}
