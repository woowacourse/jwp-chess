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
    }

    @ParameterizedTest
    @DisplayName("중복되는 체스방 이름이 있는지 반환한다.")
    @CsvSource({"first, true", "second, false"})
    void hasDuplicatedName(final String roomName, final boolean expected) {
        //given
        roomDao.saveNewRoom("first", "1234");

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
        roomDao.saveNewRoom(roomName, password);

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
        final String password = "1234";
        roomDao.saveNewRoom(roomName, password);
        final String expected = "playing";

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
        final String password = "1234";
        roomDao.saveNewRoom(roomName, password);
        roomDao.deleteRoomByName(roomName);
        //when then
        assertThatThrownBy(() -> roomDao.getGameStateByName(roomName))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
