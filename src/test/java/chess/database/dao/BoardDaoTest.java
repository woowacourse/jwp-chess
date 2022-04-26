package chess.database.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import chess.database.dao.spring.SpringBoardDao;
import chess.database.dao.spring.SpringGameDao;
import chess.database.dto.BoardDto;
import chess.database.dto.GameStateDto;
import chess.database.dto.PointDto;
import chess.database.dto.RouteDto;
import chess.domain.board.Board;
import chess.domain.board.InitialBoardGenerator;
import chess.domain.board.Point;
import chess.domain.board.Route;
import chess.domain.game.GameState;
import chess.domain.game.Ready;

@JdbcTest
@Sql("classpath:ddl.sql")
class BoardDaoTest {

    private static final String TEST_ROOM_NAME = "TESTING";
    private static final String TEST_CREATION_ROOM_NAME = "TESTING22";
    private static GameState state;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private GameDao gameDao;
    private BoardDao dao;
    private Long testId;
    private Long testCreationId;

    @BeforeEach
    void setUp() {
        gameDao = new SpringGameDao(dataSource, jdbcTemplate);
        state = new Ready();
        testId = gameDao.saveGame(GameStateDto.of(state), TEST_ROOM_NAME, "password");
        testCreationId = gameDao.saveGame(GameStateDto.of(state), TEST_CREATION_ROOM_NAME, "password");

        dao = new SpringBoardDao(jdbcTemplate);
        Board board = Board.of(new InitialBoardGenerator());
        dao.saveBoard(BoardDto.of(board.getPointPieces()), testId);
    }

    @Test
    @DisplayName("말의 위치와 종류를 저장한다.")
    public void insert() {
        // given & when
        Board board = Board.of(new InitialBoardGenerator());
        // then
        assertThatCode(() -> dao.saveBoard(BoardDto.of(board.getPointPieces()), testCreationId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말의 위치와 종류를 조회한다.")
    public void select() {
        // given & when
        final BoardDto boardDto = dao.findBoardById(testId);
        // then
        assertThat(boardDto.getPointPieces().size()).isEqualTo(32);
    }

    @Test
    @DisplayName("존재하지 않는 방을 조회하면 예외를 던진다.")
    public void selectMissingName() {
        // given & when
        final Long missingId = 999_999L;
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> dao.findBoardById(missingId));
    }

    @Test
    @DisplayName("말의 위치를 움직인다.")
    public void update() {
        // given & when
        Route route = Route.of(List.of("a2", "a4"));
        // then
        assertThatCode(() -> dao.updatePiece(RouteDto.of(route), testId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말을 삭제한다.")
    public void delete() {
        // given & when
        Point point = Point.of("b2");
        // then
        assertThatCode(() -> dao.deletePiece(PointDto.of(point), testId))
            .doesNotThrowAnyException();
    }

    @AfterEach
    void setDown() {
        dao.removeBoard(testId);
        dao.removeBoard(testCreationId);
        gameDao.removeGame(testId);
        gameDao.removeGame(testCreationId);
    }
}