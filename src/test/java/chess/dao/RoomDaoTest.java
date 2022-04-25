package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomStatusDto;
import java.util.List;
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
    @DisplayName("모든 방을 조회한다.")
    void findAll() {
        // given
        roomDao.save("name1", GameStatus.READY, Color.WHITE, "123");
        roomDao.save("name2", GameStatus.PLAYING, Color.BLACK, "1234");
        roomDao.save("name3", GameStatus.END, Color.WHITE, "12345");

        // when
        final List<RoomResponseDto> result = roomDao.findAll();

        // then
        assertThat(result.size()).isEqualTo(3);
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
    @DisplayName("방 id로 비밀번호를 조회한다.")
    void findPasswordById() {
        // given
        final String roomName = "hi";
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        final int roomId = roomDao.save(roomName, gameStatus, currentTurn, password);

        // when
        final String actual = roomDao.findPasswordById(roomId);

        // then
        assertThat(actual).isEqualTo(password);
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

    @Test
    @DisplayName("방 id로 정보를 갱신한다.")
    void updateById() {
        // given
        final String roomName = "hi";
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        final GameStatus updatedStatus = GameStatus.PLAYING;
        final Color updatedCurrentTurn = Color.BLACK;

        final int roomId = roomDao.save(roomName, gameStatus, currentTurn, password);

        // when
        final int updatedRow = roomDao.updateById(roomId, updatedStatus, updatedCurrentTurn);
        final RoomStatusDto updatedStatusDto = roomDao.findStatusById(roomId);
        final CurrentTurnDto updatedCurrentTurnDto = roomDao.findCurrentTurnById(roomId);

        // then
        assertThat(updatedRow).isEqualTo(1);
        assertThat(updatedStatusDto.getGameStatus()).isEqualTo(updatedStatus);
        assertThat(updatedCurrentTurnDto.getCurrentTurn()).isEqualTo(updatedCurrentTurn);
    }

    @Test
    @DisplayName("방 id로 상태를 갱신한다.")
    void updateStatusById() {
        // given
        final String roomName = "hi";
        final GameStatus gameStatus = GameStatus.READY;
        final Color currentTurn = Color.WHITE;
        final String password = "1q2w3e4r";

        final GameStatus updatedStatus = GameStatus.PLAYING;

        final int roomId = roomDao.save(roomName, gameStatus, currentTurn, password);

        // when
        final int updatedRow = roomDao.updateStatusById(roomId, updatedStatus);
        final RoomStatusDto updatedStatusDto = roomDao.findStatusById(roomId);

        // then
        assertThat(updatedRow).isEqualTo(1);
        assertThat(updatedStatusDto.getGameStatus()).isEqualTo(updatedStatus);
    }
}