package chess.dao;

import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.game.room.RoomPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class SpringJdbcRoomDaoTest {
    private static final String TEST_ROOM_ID = "TEST-GAME-ID";
    private static final Room TEST_ROOM = Room.from(TEST_ROOM_ID, "test game room", "1234");

    private SpringJdbcRoomDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new SpringJdbcRoomDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game("
                + "id   VARCHAR(36) NOT NULL,"
                + "turn ENUM('WHITE', 'BLACK'),"
                + "PRIMARY KEY (id))"
        );
    }

    @DisplayName("새로운 게임을 game 테이블에 생성한다.")
    @Test
    void createGame() {
        gameDao.createRoom(TEST_ROOM);
    }

    @DisplayName("게임 목록을 가져온다.")
    @Test
    void getRooms() {
        // given & when
        gameDao.createRoom(Room.create("test", "1234"));
        gameDao.createRoom(Room.create("test2", "1234"));

        // when
        int actual = gameDao.getRooms().size();

        // then
        assertThat(actual).isEqualTo(2);
    }

    @DisplayName("게임을 game 테이블로부터 제거한다.")
    @Test
    void deleteGame() {
        // given & when
        gameDao.createRoom(TEST_ROOM);

        // then
        gameDao.deleteRoom(RoomId.from(TEST_ROOM_ID), RoomPassword.createByPlainText("1234"));
    }

    @DisplayName("게임의 턴을 흰색으로 변경한다.")
    @Test
    void updateTurnToWhite() {
        // given & when
        gameDao.createRoom(TEST_ROOM);

        // then
        gameDao.updateTurnToWhite(RoomId.from(TEST_ROOM_ID));
    }

    @DisplayName("게임의 턴을 검정색으로 변경한다.")
    @Test
    void updateTurnToBlack() {
        // given & when
        gameDao.createRoom(TEST_ROOM);

        // then
        gameDao.updateTurnToBlack(RoomId.from(TEST_ROOM_ID));
    }
}
