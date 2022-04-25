package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.CurrentTurnDto;
import chess.dto.RoomStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class RoomDaoTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute(""
                + "CREATE TABLE room"
                + "("
                + "    room_id      INT         PRIMARY KEY AUTO_INCREMENT,"
                + "    name         VARCHAR(10) NOT NULL UNIQUE,"
                + "    game_status  VARCHAR(10) NOT NULL,"
                + "    current_turn VARCHAR(10) NOT NULL,"
                + "    password     VARCHAR(10) NOT NULL"
                + ")");
    }

    @Test
    @DisplayName("이름과 비밀번호가 주어지면 새로운 방을 생성한다.")
    void save() {
        // given
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        // when
        final int roomId = roomDao.save("hi", gameStatus, currentTurn, password);
        final int roomId2 = roomDao.save("hello", gameStatus, currentTurn, password);

        // then
        assertThat(roomId).isEqualTo(1);
        assertThat(roomId2).isEqualTo(2);
    }

    @Test
    @DisplayName("방 id로 현재 턴을 조회한다.")
    void findCurrentTurnById() {
        // given
        final String roomName = "hi";
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        final int roomId = roomDao.save(roomName, gameStatus, currentTurn, password);

        // when
        final CurrentTurnDto dto = roomDao.findCurrentTurnById(roomId);

        // then
        assertThat(dto.getCurrentTurn()).isEqualTo(currentTurn);
    }

    @Test
    @DisplayName("방 id로 현재 상태을 조회한다.")
    void findStatusById() {
        // given
        final String roomName = "hi";
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        final int roomId = roomDao.save(roomName, gameStatus, currentTurn, password);

        // when
        final RoomStatusDto dto = roomDao.findStatusById(roomId);

        // then
        assertThat(dto.getGameStatus()).isEqualTo(gameStatus);
    }

    @Test
    @DisplayName("방 id로 방을 삭제한다.")
    void deleteById() {
        // given
        final String roomName = "hi";
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        final int roomId = roomDao.save(roomName, gameStatus, currentTurn, password);

        // when
        final int deletedRow = roomDao.deleteById(roomId);

        // then
        assertThat(deletedRow).isEqualTo(1);
    }
}