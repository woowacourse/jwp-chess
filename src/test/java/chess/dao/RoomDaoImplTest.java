package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:roomDaoTest.sql")
class RoomDaoImplTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDaoImpl(jdbcTemplate);
        roomDao.saveNewRoom("first", "1234");
    }

    @ParameterizedTest
    @DisplayName("중복되는 체스방 이름이 있는지 반환한다.")
    @CsvSource({"first, true", "second, false"})
    void hasDuplicatedName(final String roomName, final boolean expected) {
        //actual
        final boolean actual = roomDao.hasDuplicatedName(roomName);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스방 이름에 따른 비밀번호를 반환한다.")
    void getPasswordByName() {
        //given
        final String roomName = "first";
        final String password = "1234";

        //when
        final String actual = roomDao.getPasswordByName(roomName);

        //then
        assertThat(actual).isEqualTo(password);
    }

    @Test
    @DisplayName("체스방 이름에 따른 게임 진행 상태를 반환한다.")
    void getGameStateByName() {
        //given
        final String roomName = "first";
        final String expected = "ready";

        //when
        final String actual = roomDao.getGameStateByName(roomName);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("이름에 맞는 체스방을 삭제한다.")
    void deleteRoomByName() {
        //given
        final String roomName = "first";
        roomDao.deleteRoomByName(roomName);
        //when then
        assertThatThrownBy(() -> roomDao.getGameStateByName(roomName))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("턴을 저장한다.")
    void saveTurn() {
        //given
        final String turn = "BLACK";
        final String roomName = "first";
        roomDao.saveTurn(roomName, turn);
        //when
        final String actual = roomDao.getTurn(roomName);
        //then
        assertThat(actual).isEqualTo(turn);
    }

    @Test
    @DisplayName("게임 상태를 저장한다.")
    void saveGameState() {
        //given
        final String roomName = "first";
        final String gameState = "ready";
        roomDao.saveGameState(roomName, gameState);

        //when
        final String actual = roomDao.getGameStateByName(roomName);

        //then
        assertThat(actual).isEqualTo(gameState);
    }

    @Test
    @DisplayName("체스방의 식별번호를 반환한다.")
    void getRoomId() {
        //given
        final String roomName = "first";

        //when
        final int roomId = roomDao.getRoomId(roomName);

        //then
        assertThat(roomId).isEqualTo(1);
    }
}
